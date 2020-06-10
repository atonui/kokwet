package com.example.kokwet

class UserModel {
    var user_name: String = ""
    var user_username: String = ""
    var user_phone: String = ""
    var user_email: String = ""
    var user_password: String = ""

    constructor(name: String, username: String, phone: String, email: String, password: String)
    {
        this.user_name = name
        this.user_username = username
        this.user_phone = phone
        this.user_email = email
        this.user_password = password
    }

    constructor() {}
}