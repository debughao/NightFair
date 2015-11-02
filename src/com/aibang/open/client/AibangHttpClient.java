package com.aibang.open.client;

import com.aibang.open.client.exception.AibangException;
import com.aibang.open.client.exception.AibangIOException;
import com.aibang.open.client.exception.AibangServerException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.ProtocolVersion;
import org.apache.http.TokenIterator;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionRequest;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

class AibangHttpClient {
	private HttpClient httpClient;
	private HttpHost proxy;
	private String host;
	private String apiKey;
	private String format;
	@SuppressWarnings("unused")
	private static final int DEFAULT_TIMEOUT_MILLIS = 30000;
	@SuppressWarnings("unused")
	private static final int DEFAULT_MAX_CONNECTIONS = 20;
	@SuppressWarnings("unused")
	private static final int KEEP_ALIVE_DURATION_SECS = 20;
	@SuppressWarnings("unused")
	private static final int KEEP_ALIVE_MONITOR_INTERVAL_SECS = 5;
	@SuppressWarnings("unused")
	private static final boolean DEBUG = false;

	public AibangHttpClient(String ua, String host, String apiKey, String format) {
		this.host = host;
		this.apiKey = apiKey;
		this.format = format;
		this.httpClient = createHttpClient(ua);
	}

	public void setProxy(HttpHost proxy) {
		this.proxy = proxy;
	}

	public String get(String path, NameValuePair[] params) throws AibangException {
		HttpGet httpGet = new HttpGet(createUrl(this.host, path, params));
		return execute(httpGet);
	}

	public String post(String path, NameValuePair[] params) throws AibangException {
		HttpPost httpPost = new HttpPost(createUrl(this.host, path, new NameValuePair[0]));
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(stripNulls(params), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new AibangException("post message body encode to utf8 failed", e);
		}

		return execute(httpPost);
	}

	public String post(String path, NameByteArrayPair byteArrayParam, NameValuePair[] params) throws AibangException {
		HttpPost httpPost = new HttpPost(createUrl(this.host, path, new NameValuePair[0]));
		try {
			MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.STRICT, null, null);

			if (byteArrayParam.getBytes() != null) {
				reqEntity.addPart(byteArrayParam.getName(), new ByteArrayBody(byteArrayParam.getBytes(), null));
			}

			for (NameValuePair param : params) {
				if (param.getValue() != null) {
					reqEntity.addPart(param.getName(), new StringBody(param.getValue(), Charset.forName("UTF-8")));
				}

			}

			httpPost.setEntity(reqEntity);
		} catch (UnsupportedEncodingException e) {
			throw new AibangException("post message body encode to utf8 failed", e);
		}

		return execute(httpPost);
	}

	private String createUrl(String host, String path, NameValuePair[] params) {
		List<NameValuePair> params_list = stripNulls(params);
		params_list.add(new BasicNameValuePair("app_key", this.apiKey));
		params_list.add(new BasicNameValuePair("alt", this.format));
		String querystring = URLEncodedUtils.format(params_list, "UTF-8");
		StringBuilder query_builder = new StringBuilder();
		query_builder.append(host).append(path).append("?").append(querystring);

		return query_builder.toString();
	}

	private List<NameValuePair> stripNulls(NameValuePair[] params) {
		List<NameValuePair> params_list = new ArrayList<NameValuePair>();
		for (int i = 0; i < params.length; i++) {
			NameValuePair param = params[i];
			if (param.getValue() != null) {
				params_list.add(param);
			}
		}
		return params_list;
	}

	private HttpClient createHttpClient(String userAgent) {
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 30000);

		HttpConnectionParams.setSoTimeout(httpParams, 30000);

		HttpConnectionParams.setSocketBufferSize(httpParams, 8192);
		HttpProtocolParams.setUserAgent(httpParams, userAgent);

		HttpParams connParams = new BasicHttpParams();
		ConnManagerParams.setMaxTotalConnections(connParams, 20);

		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));

		MyClientConnManager cm = new MyClientConnManager(connParams, schemeRegistry);

		DefaultHttpClient client = new DefaultHttpClient(cm, httpParams) {
			protected ConnectionKeepAliveStrategy createConnectionKeepAliveStrategy() {
				return new AibangHttpClient.MyKeepAliveStrategy();
			}

			protected ConnectionReuseStrategy createConnectionReuseStrategy() {
				return new AibangHttpClient.MyConnectionReuseStrategy();
			}
		};
		client.addRequestInterceptor(new HttpRequestInterceptor() {
			public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
				if (!request.containsHeader("Accept-Encoding"))
					request.addHeader("Accept-Encoding", "gzip");
			}
		});
		client.addResponseInterceptor(new HttpResponseInterceptor() {
			public void process(HttpResponse response, HttpContext context) throws HttpException, IOException {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					Header ceheader = entity.getContentEncoding();
					if (ceheader != null) {
						HeaderElement[] codecs = ceheader.getElements();
						for (HeaderElement codec : codecs)
							if (codec.getName().equalsIgnoreCase("gzip")) {
								response.setEntity(new AibangHttpClient.GzipDecompressingEntity(response.getEntity()));

								return;
							}
					}
				}
			}
		});
		return client;
	}

	private String execute(HttpUriRequest req) throws AibangException {
		String result = null;
		int statusCode = 200;
		try {
			ConnRouteParams.setDefaultProxy(this.httpClient.getParams(), this.proxy);
			HttpResponse response = this.httpClient.execute(req);
			statusCode = response.getStatusLine().getStatusCode();
			result = EntityUtils.toString(response.getEntity());
		} catch (IOException e) {
			throw new AibangIOException(e);
		} catch (Exception e) {
			throw new AibangException("Unknown aibang exception", e);
		}
		if (statusCode != 200) {
			throw new AibangServerException(statusCode, result);
		}
		return result;
	}

	private static class GzipDecompressingEntity extends HttpEntityWrapper {
		public GzipDecompressingEntity(HttpEntity entity) {
			super(entity);
		}

		public InputStream getContent() throws IOException, IllegalStateException {
			InputStream wrappedin = this.wrappedEntity.getContent();

			return new GZIPInputStream(wrappedin);
		}

		public long getContentLength() {
			return -1L;
		}
	}

	private static class IdleConnectionCloserThread extends Thread {
		private final AibangHttpClient.MyClientConnManager manager;
		private final int idleTimeoutSeconds;
		private final int checkIntervalMs;
		private static IdleConnectionCloserThread thread = null;

		public IdleConnectionCloserThread(AibangHttpClient.MyClientConnManager manager, int idleTimeoutSeconds,
				int checkIntervalSeconds) {
			this.manager = manager;
			this.idleTimeoutSeconds = idleTimeoutSeconds;
			this.checkIntervalMs = (checkIntervalSeconds * 1000);
		}

		public static synchronized void ensureRunning(AibangHttpClient.MyClientConnManager manager,
				int idleTimeoutSeconds, int checkIntervalSeconds) {
			if (thread == null) {
				thread = new IdleConnectionCloserThread(manager, idleTimeoutSeconds, checkIntervalSeconds);

				thread.start();
			}
		}

		public void run() {
			try {
				while (true) {
					synchronized (this) {
						wait(this.checkIntervalMs);
					}
					this.manager.closeExpiredConnections();
					this.manager.closeIdleConnections(this.idleTimeoutSeconds, TimeUnit.SECONDS);

					synchronized (IdleConnectionCloserThread.class) {
						if (this.manager.getConnectionsInPool() == 0) {
							thread = null;
							return;
						}
					}
				}
			} catch (InterruptedException e) {
				thread = null;
			}
		}
	}

	private static class MyConnectionReuseStrategy extends DefaultConnectionReuseStrategy {
		public boolean keepAlive(HttpResponse response, HttpContext context) {
			if (response == null) {
				throw new IllegalArgumentException("HTTP response may not be null.");
			}

			if (context == null) {
				throw new IllegalArgumentException("HTTP context may not be null.");
			}

			ProtocolVersion ver = response.getStatusLine().getProtocolVersion();
			Header teh = response.getFirstHeader("Transfer-Encoding");
			if (teh != null) {
				if (!"chunked".equalsIgnoreCase(teh.getValue()))
					return false;
			} else {
				Header[] clhs = response.getHeaders("Content-Length");

				if ((clhs == null) || (clhs.length != 1)) {
					return false;
				}
				Header clh = clhs[0];
				try {
					int contentLen = Integer.parseInt(clh.getValue());
					if (contentLen < 0)
						return false;
				} catch (NumberFormatException ex) {
					return false;
				}

			}

			HeaderIterator hit = response.headerIterator("Connection");
			if (!hit.hasNext()) {
				hit = response.headerIterator("Proxy-Connection");
			}

			if (hit.hasNext()) {
				try {
					TokenIterator ti = createTokenIterator(hit);
					boolean keepalive = false;
					while (ti.hasNext()) {
						String token = ti.nextToken();
						if ("Close".equalsIgnoreCase(token))
							return false;
						if ("Keep-Alive".equalsIgnoreCase(token)) {
							keepalive = true;
						}
					}
					if (keepalive) {
						return true;
					}

				} catch (ParseException px) {
					return false;
				}

			}

			return !ver.lessEquals(HttpVersion.HTTP_1_0);
		}
	}

	private static class MyKeepAliveStrategy implements ConnectionKeepAliveStrategy {
		public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
			long timeout = 20000L;

			HeaderElementIterator i = new BasicHeaderElementIterator(response.headerIterator("Keep-Alive"));

			while (i.hasNext()) {
				HeaderElement element = i.nextElement();
				String name = element.getName();
				String value = element.getValue();
				if ((value != null) && (name.equalsIgnoreCase("timeout")))
					try {
						timeout = Math.min(timeout, Long.parseLong(value) * 1000L);
					} catch (NumberFormatException e) {
					}
			}
			return timeout;
		}
	}

	private static class MyClientConnManager extends ThreadSafeClientConnManager {
		public MyClientConnManager(HttpParams params, SchemeRegistry schreg) {
			super(params, schreg);
		}

		public ClientConnectionRequest requestConnection(HttpRoute route, Object state) {
			AibangHttpClient.IdleConnectionCloserThread.ensureRunning(this, 20, 5);

			return super.requestConnection(route, state);
		}
	}

	public static class NameByteArrayPair {
		private String name;
		private String fileName;
		private byte[] bytes;

		public NameByteArrayPair(String name, String fileName, byte[] bytes) {
			this.name = name;
			this.fileName = fileName;
			this.bytes = bytes;
		}

		public String getName() {
			return this.name;
		}

		public String getFileName() {
			return this.fileName;
		}

		public byte[] getBytes() {
			return this.bytes;
		}
	}
}