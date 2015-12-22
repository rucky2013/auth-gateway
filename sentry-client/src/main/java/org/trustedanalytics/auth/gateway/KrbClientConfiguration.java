/**
 * Copyright (c) 2015 Intel Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.trustedanalytics.auth.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.trustedanalytics.hadoop.config.client.Configurations;
import org.trustedanalytics.hadoop.config.client.Property;
import org.trustedanalytics.hadoop.config.client.ServiceInstanceConfiguration;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.security.auth.login.LoginException;

@Component
@Profile("sentry-auth-gateway")
public class KrbClientConfiguration {

  private String kdc;

  private String realm;

  @Value("${sentry.client.superUser}")
  private String superUser;

  @Value("${sentry.client.clientKeyTab}")
  private String clientKeyTab;

  private String keyTabPath;

  private static final String KRB_CONF_SERVICE_NAME = "kerberos-service";

  private static final String KRB_PRINC_TO_SYS_USER_NAME_RULES = "hadoop.security.auth_to_local";

  @PostConstruct
  public void initialize() throws IOException, LoginException {
    ServiceInstanceConfiguration krbServiceConf =
        Configurations.newInstanceFromEnv().getServiceConfig(KRB_CONF_SERVICE_NAME);
    kdc = krbServiceConf.getProperty(Property.KRB_KDC).get();
    realm = krbServiceConf.getProperty(Property.KRB_REALM).get();
    keyTabPath = KeyTab.createInstance(clientKeyTab, superUser).getFullKeyTabFilePath();
  }

  public String getKdc() {
    return kdc;
  }

  public String getRealm() {
    return realm;
  }

  public String getSuperUser() {
    return superUser;
  }

  public String getKeyTabPath() {
    return keyTabPath;
  }
}