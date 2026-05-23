package edu.cit.boquia.triptab.data


// short term storage: for app-related purposes
// SharedPref: long term-storage
data class User (
    var name: String = "",
    var email: String = "",
    var birthDate: String = "",
    var phoneNo: String = "",
    var password: String = "",
    var profileImageUri: String? = null
)