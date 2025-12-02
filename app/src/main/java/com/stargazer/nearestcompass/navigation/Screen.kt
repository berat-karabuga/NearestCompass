package com.stargazer.nearestcompass.navigation


//sealed class sınırlı sayıda seçenek demek
sealed class Screen(val route: String){

    object Welcome : Screen("welcome")

    object Compass : Screen("compass")

}

//her ekrana bir adres veriyoruz böylece navigation bu adresleri kullanrak ekranları göstericek