package org.knowm.xchange.cexio.service.polling;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.cexio.CexIOAdapters;
import org.knowm.xchange.cexio.dto.trade.CexIOOrder;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.MarketOrder;
import org.knowm.xchange.dto.trade.OpenOrders;
import org.knowm.xchange.dto.trade.UserTrades;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.exceptions.NotAvailableFromExchangeException;
import org.knowm.xchange.exceptions.NotYetImplementedForExchangeException;
import org.knowm.xchange.service.polling.trade.PollingTradeService;
import org.knowm.xchange.service.polling.trade.params.TradeHistoryParams;

/**
 * Author: brox Since: 2/6/14
 */

public class CexIOTradeService extends CexIOTradeServiceRaw implements PollingTradeService {

  /**
   * Constructor
   *
   * @param exchange
   */
  public CexIOTradeService(Exchange exchange) {

    super(exchange);
  }

  @Override
  public OpenOrders getOpenOrders() throws IOException {

    List<CexIOOrder> cexIOOrderList = getCexIOOpenOrders();

    return CexIOAdapters.adaptOpenOrders(cexIOOrderList);
  }

  @Override
  public String placeMarketOrder(MarketOrder marketOrder) throws IOException {

    throw new NotAvailableFromExchangeException();
  }

  @Override
  public String placeLimitOrder(LimitOrder limitOrder) throws IOException {

    CexIOOrder order = placeCexIOLimitOrder(limitOrder);

    return Long.toString(order.getId());
  }

  @Override
  public boolean cancelOrder(String orderId) throws IOException {

    return cancelCexIOOrder(orderId);
  }

  @Override
  public UserTrades getTradeHistory(TradeHistoryParams params) throws IOException {

    throw new NotAvailableFromExchangeException();
  }

  @Override
  public org.knowm.xchange.service.polling.trade.params.TradeHistoryParams createTradeHistoryParams() {
    throw new NotAvailableFromExchangeException();
  }

  @Override
  public Collection<Order> getOrder(String... orderIds)
      throws ExchangeException, NotAvailableFromExchangeException, NotYetImplementedForExchangeException, IOException {
    throw new NotYetImplementedForExchangeException();
  }

}
