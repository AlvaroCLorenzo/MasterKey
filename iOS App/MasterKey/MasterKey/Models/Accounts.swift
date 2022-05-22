//
//  Accounts.swift
//  MasterKey
//
//  Created by Wembley on 5/4/22.
//

import Foundation

struct Accounts: Codable {
    var socialNetworks: [SocialNetwork]
    var creditCards: [CreditCard]
    var cryptoWallets: [CryptoWallet]

    init() {
        socialNetworks = [SocialNetwork]()
        creditCards = [CreditCard]()
        cryptoWallets = [CryptoWallet]()
    }
}


