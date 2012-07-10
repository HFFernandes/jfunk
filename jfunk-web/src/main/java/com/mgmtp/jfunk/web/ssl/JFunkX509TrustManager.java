package com.mgmtp.jfunk.web.ssl;

import static com.google.common.base.Preconditions.checkArgument;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link X509TrustManager} implementation for SSL and client authentication. Acts as a decorator to
 * the {@link X509TrustManager} passed in by the constructor in order to add logging.
 * 
 * @version $Id$
 */
public final class JFunkX509TrustManager implements X509TrustManager {
	private final Logger log = LoggerFactory.getLogger(getClass());

	private final X509TrustManager defaultTrustManager;

	public JFunkX509TrustManager(final X509TrustManager defaultTrustManager) {
		checkArgument(defaultTrustManager != null, "Trust manager may not be null");
		this.defaultTrustManager = defaultTrustManager;
	}

	@Override
	public void checkClientTrusted(final X509Certificate[] certificates, final String authType) throws CertificateException {
		if (log.isDebugEnabled() && certificates != null) {
			for (int i = 0; i < certificates.length; ++i) {
				log.debug("Client certificate {}:", i + 1);
				logCertificate(certificates[i]);
			}
		}
		defaultTrustManager.checkClientTrusted(certificates, authType);
	}

	@Override
	public void checkServerTrusted(final X509Certificate[] certificates, final String authType) throws CertificateException {
		if (log.isDebugEnabled() && certificates != null) {
			for (int i = 0; i < certificates.length; ++i) {
				log.debug("Server certificate {}:", i + 1);
				logCertificate(certificates[i]);
			}
		}
		defaultTrustManager.checkServerTrusted(certificates, authType);
	}

	private void logCertificate(final X509Certificate cert) {
		log.debug("  Subject DN: {}", cert.getSubjectDN());
		log.debug("  Signature algorithm name: {}", cert.getSigAlgName());
		log.debug("  Valid from: {}", cert.getNotBefore());
		log.debug("  Valid until: {}", cert.getNotAfter());
		log.debug("  Issuer DN: {}", cert.getIssuerDN());
	}

	/**
	 * @see javax.net.ssl.X509TrustManager#getAcceptedIssuers()
	 */
	@Override
	public X509Certificate[] getAcceptedIssuers() {
		return this.defaultTrustManager.getAcceptedIssuers();
	}
}