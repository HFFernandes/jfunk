/*
 *  Copyright (C) 2013 mgm technology partners GmbH, Munich.
 *
 *  See the LICENSE file distributed with this work for additional
 *  information regarding copyright ownership and intellectual property rights.
 */
package com.mgmtp.jfunk.web.ssl;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/**
 * @author rnaegele
 */
public final class JFunkTrustAllManager implements X509TrustManager {

	@Override
	public void checkClientTrusted(final X509Certificate[] certificates, final String authType) throws CertificateException {
		// no-op
	}

	@Override
	public void checkServerTrusted(final X509Certificate[] certificates, final String authType) throws CertificateException {
		// no-op
	}

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		return null;
	}
}