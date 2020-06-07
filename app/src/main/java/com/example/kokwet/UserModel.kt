package com.example.kokwet

class UserModel {
    var user_name: String = ""
    var user_phone: String = ""
    var user_id: String = ""
    var user_email: String = ""
    var user_password: String = ""

    constructor(name: String, phone: String, id: String, email: String, password: String)
    {
        this.user_name = name
        this.user_phone = phone
        this.user_email = email
        this.user_id = id
        this.user_password = password
    }

    constructor() {}
}