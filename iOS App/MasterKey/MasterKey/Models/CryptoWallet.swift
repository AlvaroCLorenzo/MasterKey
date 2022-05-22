//
//  CryptoWallet.swift
//  MasterKey
//
//  Created by Wembley on 5/4/22.
//

import Foundation

struct CryptoWallet: Codable {
    var id: Int
    var walletName: String
    var walletType: String
    var email: String
    var password: String
    var publicKey: String
    var privateKey: String
}
