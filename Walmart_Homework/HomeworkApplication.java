/*
  author @sudhiramara
*/

import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.*;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeworkApplication {

	/*
      getRecommendations(String productName) takes the productName given by the user and returns the 
      first 10 ranked product recommendations based on previous customer rating. In the process it 
      provides inputs and fetches values from Search API , Product Recommendations API and Reviews API of
      the WALMART OPEN API.      
    */

	public Set<Entry<Integer, ReviewVO>> getRecommendations(String productName) throws Exception {

		HashMap<Integer, ReviewVO> prodReviewMap = new HashMap<Integer, ReviewVO>();
		int[] prodRecommendations = null;
		int productID = searchAPI(productName);
		Set<Entry<Integer,ReviewVO>> sortedprodRevSet = null;
		if (productID != 0) {
			prodRecommendations = productRecommendationsAPI(productID);
		}
		if (prodRecommendations != null) {
			for (int i = 0; i < prodRecommendations.length; i++) {
				JSONArray reviews = reviewAPI(prodRecommendations[i]);
				ReviewVO revVO = reviewRatingCal(reviews);
				prodReviewMap.put(prodRecommendations[i], revVO);
			}
			HashMap<Integer, ReviewVO> sortedprodReviewMap = sortRating(prodReviewMap);
			sortedprodRevSet = sortedprodReviewMap.entrySet();

		}
		return sortedprodRevSet;
	}


    /*
    searchAPI takes the item name entered by the user and returns the first item id from the list of items available.
    JSON Object returned by walmart Search API is manipulated to fetch the item id 
    */

	public int searchAPI(String productName) throws JSONException, Exception {
		Client client = ClientBuilder.newClient();
		URL url = new URL("http://api.walmartlabs.com/v1/search?apiKey=ts9qjxzb8ddd2n56nxtdczw4&query="
				+ URLEncoder.encode(productName, "UTF-8"));
		WebTarget target = client.target(url.toString());
		int productID = 0;
		try {
			String products = target.request(MediaType.APPLICATION_JSON).get(String.class);
			JSONObject jobj = new JSONObject(products);
			JSONArray items = jobj.getJSONArray("items");
			productID = items.getJSONObject(0).getInt("itemId");
		} catch (JSONException jex) {
			System.out.println("There are no items found for your entry");
		}
		return productID;

	}

	/*
    productRecommendationsAPI takes the first item id that was returned by the searchAPI and finds the first 10
    product recommendations.
    JSON Object is manipulated to fetch the first 10 product recommendations.      
    */

	public int[] productRecommendationsAPI(int productID) throws JSONException {
		Client client = ClientBuilder.newClient();
		WebTarget target = client
				.target("http://api.walmartlabs.com/v1/nbp?apiKey=ts9qjxzb8ddd2n56nxtdczw4&itemId=" + productID);
		String products = target.request(MediaType.APPLICATION_JSON).get(String.class);
		int recommendations[] = new int[10];
		try {
			JSONArray items = new JSONArray(products);
			for (int i = 0; i < 10; i++) {
				recommendations[i] = items.getJSONObject(i).getInt("itemId");
			}
		} catch (JSONException jex) {
			System.out.println("There are no recommendations found for this product");
			return null;

		}
		return recommendations;
	}

    
    /*
     reviewAPI takes each productID returned by the productrecommendations and gives the reviews
     to each of those 10 products. 
    */

	public JSONArray reviewAPI(int recommendationID) throws JSONException {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://api.walmartlabs.com/v1/reviews/" + recommendationID
				+ "?apiKey=ts9qjxzb8ddd2n56nxtdczw4&format=json");
		JSONArray reviews = null;
		try {
			String product = target.request(MediaType.APPLICATION_JSON).get(String.class);
			JSONObject jobj = new JSONObject(product);
			reviews = jobj.getJSONArray("reviews");
		} catch (JSONException jex) {
			System.out.println("There are no reviews found for this product");
		}
		return reviews;
	}


    /*
      reviewRatingCal takes reviews of every product and returns overall rating for each review of each product
      and calculates average rating for each product based on reviews and ratings. The count of reviews for each 
      product is taken to get the better product recommendations first     
    */

	public ReviewVO reviewRatingCal(JSONArray reviews) throws JSONException {
		JSONObject ratingObj = new JSONObject();
		float sum = 0.0f;
		String name = null;
		ArrayList<Integer> ratingList = new ArrayList<Integer>();
		for (int i = 0; i < reviews.length(); i++) {
			ratingObj = reviews.getJSONObject(i).getJSONObject("overallRating");
			if (i == 0) {
				name = reviews.getJSONObject(i).getString("name");
			}

			ratingList.add(ratingObj.getInt("rating"));
		}
		int i;
		double Avg = 0.0f;
		for (i = 0; i < ratingList.size(); i++) {
			sum = sum + ratingList.get(i);
		}
		if (ratingList.size() != 0) {
			Avg = Double.parseDouble(new DecimalFormat("#.#").format(sum / ratingList.size()));
		} else {
			Avg=0;
		}
		ReviewVO revVO = new ReviewVO();
		revVO.setName(name);
		revVO.setAverage(Avg);
		revVO.setRevCount(ratingList.size());

		return revVO;
	}

	/*
     products are rated based on the ratings and the number of reviews
    */


	public HashMap<Integer, ReviewVO> sortRating(HashMap<Integer, ReviewVO> map) {
		List<Entry<Integer, ReviewVO>> ls = new ArrayList<Entry<Integer, ReviewVO>>(map.entrySet());
		Collections.sort(ls, new Comparator<Entry<Integer, ReviewVO>>() {

			@Override
			public int compare(Entry<Integer, ReviewVO> R1, Entry<Integer, ReviewVO> R2) {
				double val1 = R1.getValue().average;
				double val2 = R2.getValue().average;
				if (val1 == val2) {
					return Double.compare(R2.getValue().revCount, R1.getValue().revCount);
				} else
					return Double.compare(R2.getValue().average, R1.getValue().average);
			}
		});

		HashMap<Integer, ReviewVO> sortedReviewMap = new LinkedHashMap<Integer, ReviewVO>();
		for (Iterator<Entry<Integer, ReviewVO>> itr = ls.iterator(); itr.hasNext();) {
			Entry<Integer, ReviewVO> entry = itr.next();
			Integer io = new Integer((int) entry.getKey());
			sortedReviewMap.put(io, (ReviewVO) entry.getValue());
		}
		return sortedReviewMap;

	}
}
