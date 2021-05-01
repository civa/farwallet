package com.alphawallet.app.interact;

import com.alphawallet.app.repository.EthereumNetworkRepositoryType;
import com.alphawallet.app.repository.WalletRepositoryType;
import com.alphawallet.app.util.BalanceUtils;
import com.alphawallet.app.entity.Wallet;

import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class GetDefaultWalletBalance {

    private final WalletRepositoryType walletRepository;
    private final EthereumNetworkRepositoryType ethereumNetworkRepository;

    public GetDefaultWalletBalance(
            WalletRepositoryType walletRepository,
            EthereumNetworkRepositoryType ethereumNetworkRepository) {
        this.walletRepository = walletRepository;
        this.ethereumNetworkRepository = ethereumNetworkRepository;
    }

    public Single<Map<String, String>> get(Wallet wallet) {
        return walletRepository.balanceInWei(wallet)
                .flatMap(ethBalance -> {
                    Map<String, String> balances = new HashMap<>();
                    balances.put(
                            ethereumNetworkRepository.getDefaultNetwork().symbol,
                            BalanceUtils.weiToEth(ethBalance)
                                    .setScale(4, RoundingMode.DOWN)
                                .stripTrailingZeros().toPlainString());
                    return Single.just(balances);
                })
                .observeOn(AndroidSchedulers.mainThread());
    }


}