package com.empayre.dominator.dao.party.iface;

import com.empayre.dominator.data.ShopTermSetDataObject;
import com.empayre.dominator.data.TerminalTermSetDataObject;
import com.empayre.dominator.data.WalletTermSetDataObject;
import org.jooq.Condition;

import java.util.List;

public interface TermSetDao {

    List<ShopTermSetDataObject> getShopTermSets(Condition condition, int limit);

    List<WalletTermSetDataObject> getWalletTermSets(Condition condition, int limit);

    List<TerminalTermSetDataObject> getTerminalTermSets(Condition condition, int limit);

}
