//
//  SecretBox.swift
//  MasterKey
//
//  Created by Wembley on 5/4/22.
//

import Foundation

struct SecretBox: Codable {
    var user: User
    var accounts: Accounts
    
    init(user: User, accounts: Accounts) {
        self.user = user
        self.accounts = accounts
    }
}
