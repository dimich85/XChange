/**
 * Copyright (C) 2012 - 2013 Xeiam LLC http://xeiam.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.xeiam.xchange.mtgox.v2.service.account.polling;

import java.math.BigDecimal;

import com.xeiam.xchange.ExchangeSpecification;
import com.xeiam.xchange.dto.account.AccountInfo;
import com.xeiam.xchange.mtgox.MtGoxUtils;
import com.xeiam.xchange.mtgox.v2.MtGoxAdapters;
import com.xeiam.xchange.mtgox.v2.MtGoxV2;
import com.xeiam.xchange.mtgox.v2.dto.account.MtGoxAccountInfo;
import com.xeiam.xchange.mtgox.v2.dto.account.MtGoxBitcoinDepositAddress;
import com.xeiam.xchange.mtgox.v2.dto.account.MtGoxWithdrawalResponse;
import com.xeiam.xchange.service.account.polling.PollingAccountService;
import com.xeiam.xchange.service.streaming.BasePollingExchangeService;
import com.xeiam.xchange.utils.Assert;

import si.mazi.rescu.ParamsDigest;
import si.mazi.rescu.RestProxyFactory;

/**
 * <p>
 * XChange service to provide the following to {@link com.xeiam.xchange.Exchange}:
 * </p>
 * <ul>
 * <li>MtGox specific methods to handle account-related operations</li>
 * </ul>
 */
public class MtGoxPollingAccountService extends BasePollingExchangeService implements PollingAccountService {

  /**
   * Configured from the super class reading of the exchange specification
   */
  private final MtGoxV2 mtGoxV2;
  private ParamsDigest signatureCreator;

  /**
   * Constructor
   * 
   * @param exchangeSpecification
   */
  public MtGoxPollingAccountService(ExchangeSpecification exchangeSpecification) {

    super(exchangeSpecification);

    Assert.notNull(exchangeSpecification.getSslUri(), "Exchange specification URI cannot be null");
    this.mtGoxV2 = RestProxyFactory.createProxy(MtGoxV2.class, exchangeSpecification.getSslUri());
    signatureCreator = MtGoxV2Digest.createInstance(exchangeSpecification.getSecretKey());
  }

  @Override
  public AccountInfo getAccountInfo() {

    MtGoxAccountInfo mtGoxAccountInfo = mtGoxV2.getAccountInfo(exchangeSpecification.getApiKey(), signatureCreator, MtGoxUtils.getNonce(), true);
    return MtGoxAdapters.adaptAccountInfo(mtGoxAccountInfo);
  }

  @Override
  public String withdrawFunds(BigDecimal amount, String address) {

    // TODO this will probably cause issues with SEK and JPY. Use MtGox Utils.java instead!!!
    MtGoxWithdrawalResponse result = mtGoxV2.withdrawBtc(exchangeSpecification.getApiKey(), signatureCreator, MtGoxUtils.getNonce(), address, amount.multiply(
        new BigDecimal(MtGoxUtils.BTC_VOLUME_AND_AMOUNT_INT_2_DECIMAL_FACTOR)).intValue(), 1, false, false);
    return result.getTransactionId();
  }

  @Override
  public String requestBitcoinDepositAddress(final String... arguments) {

    String description = arguments[0];
    String notificationUrl = arguments[1];
    MtGoxBitcoinDepositAddress mtGoxBitcoinDepositAddress = mtGoxV2.requestDepositAddress(exchangeSpecification.getApiKey(), signatureCreator, MtGoxUtils.getNonce(), description, notificationUrl);

    return mtGoxBitcoinDepositAddress.getAddres();
  }

}
