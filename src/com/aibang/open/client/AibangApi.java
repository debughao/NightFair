package com.aibang.open.client;

import com.aibang.open.client.exception.AibangException;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class AibangApi {
	@SuppressWarnings("unused")
	private static final String HOST = "http://openapi.aibang.com";
	@SuppressWarnings("unused")
	private static final String SDK = "AibangAndroidOpenSdk_v1.0";
	private AibangHttpClient client;

	public AibangApi(String apiKey) {
		this(apiKey, "json");
	}

	public AibangApi(String apiKey, String format) {
		this.client = new AibangHttpClient("AibangAndroidOpenSdk_v1.0", "http://openapi.aibang.com", apiKey, format);
	}

	public void setProxy(HttpHost proxy) {
		this.client.setProxy(proxy);
	}

	public String search(String city, String a, Float lng, Float lat, String q, String cate, Integer rc, Integer as,
			Integer from, Integer to) throws AibangException {
		return this.client.get("/search",
				new NameValuePair[] { new BasicNameValuePair("city", city), new BasicNameValuePair("a", a),
						new BasicNameValuePair("lng", getFloatParam(lng)),
						new BasicNameValuePair("lat", getFloatParam(lat)), new BasicNameValuePair("q", q),
						new BasicNameValuePair("cate", cate), new BasicNameValuePair("rc", getIntegerParam(rc)),
						new BasicNameValuePair("as", getIntegerParam(as)),
						new BasicNameValuePair("from", getIntegerParam(from)),
						new BasicNameValuePair("to", getIntegerParam(to)) });
	}

	public String locate(String city, String addr) throws AibangException {
		return this.client.get("/locate",
				new NameValuePair[] { new BasicNameValuePair("city", city), new BasicNameValuePair("addr", addr) });
	}

	public String biz(String bid) throws AibangException {
		return this.client.get("/biz/" + bid, new NameValuePair[0]);
	}

	public String comments(String bid, Integer from, Integer to) throws AibangException {
		return this.client.get("/biz/" + bid + "/comments",
				new NameValuePair[] { new BasicNameValuePair("from", getIntegerParam(from)),
						new BasicNameValuePair("to", getIntegerParam(to)) });
	}

	public String postComment(String uname, String bid, Integer score, Integer cost, String content)
			throws AibangException {
		return this.client.post("/biz/" + bid + "/comment",
				new NameValuePair[] { new BasicNameValuePair("uname", uname),
						new BasicNameValuePair("score", getIntegerParam(score)),
						new BasicNameValuePair("cost", getIntegerParam(cost)),
						new BasicNameValuePair("content", content) });
	}

	public String pics(String bid, Integer from, Integer to) throws AibangException {
		return this.client.get("/biz/" + bid + "/pics",
				new NameValuePair[] { new BasicNameValuePair("from", getIntegerParam(from)),
						new BasicNameValuePair("to", getIntegerParam(to)) });
	}

	public String postPic(String uname, String bid, String title, byte[] data) throws AibangException {
		return this.client.post("/biz/" + bid + "/pic", new AibangHttpClient.NameByteArrayPair("data", null, data),
				new NameValuePair[] { new BasicNameValuePair("uname", uname), new BasicNameValuePair("title", title) });
	}

	public String modifyBiz(String uname, String bid, String name, Integer status, String tel, String city,
			String county, String addr, String desc, Float lng, Float lat, String cate, String worktime, String cost,
			String site_url) throws AibangException {
		return this.client.post("/biz/" + bid,
				new NameValuePair[] { new BasicNameValuePair("uname", uname), new BasicNameValuePair("name", name),
						new BasicNameValuePair("status", getIntegerParam(status)), new BasicNameValuePair("tel", tel),
						new BasicNameValuePair("city", city), new BasicNameValuePair("country", county),
						new BasicNameValuePair("addr", addr), new BasicNameValuePair("desc", desc),
						new BasicNameValuePair("lng", getFloatParam(lng)),
						new BasicNameValuePair("lat", getFloatParam(lat)), new BasicNameValuePair("cate", cate),
						new BasicNameValuePair("worktime", worktime), new BasicNameValuePair("cost", cost),
						new BasicNameValuePair("site_url", site_url) });
	}

	public String addBiz(String uname, String name, String tel, String city, String county, String cate, String addr,
			String desc, Float lng, Float lat, String worktime, String cost, String site_url) throws AibangException {
		return this.client.post("/biz",
				new NameValuePair[] { new BasicNameValuePair("uname", uname), new BasicNameValuePair("name", name),
						new BasicNameValuePair("tel", tel), new BasicNameValuePair("city", city),
						new BasicNameValuePair("county", county), new BasicNameValuePair("cate", cate),
						new BasicNameValuePair("addr", addr), new BasicNameValuePair("desc", desc),
						new BasicNameValuePair("lng", getFloatParam(lng)),
						new BasicNameValuePair("lat", getFloatParam(lat)), new BasicNameValuePair("worktime", worktime),
						new BasicNameValuePair("cost", cost), new BasicNameValuePair("site_url", site_url) });
	}

	public String bus(String city, String start_addr, String end_addr, Float start_lng, Float start_lat, Float end_lng,
			Float end_lat, Integer rc, Integer count, Integer with_xys) throws AibangException {
		return this.client.get("/bus/transfer",
				new NameValuePair[] { new BasicNameValuePair("city", city),
						new BasicNameValuePair("start_addr", start_addr), new BasicNameValuePair("end_addr", end_addr),
						new BasicNameValuePair("start_lng", getFloatParam(start_lng)),
						new BasicNameValuePair("start_lat", getFloatParam(start_lat)),
						new BasicNameValuePair("end_lng", getFloatParam(end_lng)),
						new BasicNameValuePair("end_lat", getFloatParam(end_lat)),
						new BasicNameValuePair("rc", getIntegerParam(rc)),
						new BasicNameValuePair("count", getIntegerParam(count)),
						new BasicNameValuePair("with_xys", getIntegerParam(with_xys)) });
	}

	public String busLines(String city, String q, Integer with_xys) throws AibangException {
		return this.client.get("/bus/lines", new NameValuePair[] { new BasicNameValuePair("city", city),
				new BasicNameValuePair("q", q), new BasicNameValuePair("with_xys", getIntegerParam(with_xys)) });
	}

	public String busStats(String city, String q) throws AibangException {
		return this.client.get("/bus/stats",
				new NameValuePair[] { new BasicNameValuePair("city", city), new BasicNameValuePair("q", q) });
	}

	public String busStatsXy(String city, Float lng, Float lat, Integer dist) throws AibangException {
		return this.client.get("/bus/stats_xy",
				new NameValuePair[] { new BasicNameValuePair("city", city),
						new BasicNameValuePair("lng", getFloatParam(lng)),
						new BasicNameValuePair("lat", getFloatParam(lat)),
						new BasicNameValuePair("dist", getIntegerParam(dist)) });
	}

	private String getFloatParam(Float value) {
		return value != null ? String.valueOf(value) : null;
	}

	private String getIntegerParam(Integer value) {
		return value != null ? String.valueOf(value) : null;
	}
}