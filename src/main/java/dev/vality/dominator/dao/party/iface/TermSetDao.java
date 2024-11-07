package dev.vality.dominator.dao.party.iface;

import dev.vality.dominator.data.ShopTermSetDataObject;
import dev.vality.dominator.data.TerminalTermSetDataObject;
import dev.vality.dominator.data.WalletTermSetDataObject;
import org.jooq.Condition;

import java.util.List;

public interface TermSetDao {

    List<ShopTermSetDataObject> getShopTermSets(Condition condition, int limit);

    List<WalletTermSetDataObject> getWalletTermSets(Condition condition, int limit);

    List<TerminalTermSetDataObject> getTerminalTermSets(Condition condition, int limit);

}
