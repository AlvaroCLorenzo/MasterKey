//
//  CreditCards.swift
//  MasterKey
//
//  Created by Wembley on 5/4/22.
//

import Foundation

struct CreditCard: Codable {
    var id: Int
    var nameOfBank: String
    var typeOfCard: String
    var userName: String
    var numberOfCard: Int
    var expirationDate: String
    var CCV: Int
    var PIN: Int
}
