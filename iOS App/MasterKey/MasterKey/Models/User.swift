//
//  User.swift
//  MasterKey
//
//  Created by Wembley on 5/4/22.
//

import Foundation

struct User: Codable {
    var nickName: String
    var masterPassword: String
    
    init(nickname: String, masterPassword: String) {
        self.nickName = nickname
        self.masterPassword = masterPassword
    }
}
