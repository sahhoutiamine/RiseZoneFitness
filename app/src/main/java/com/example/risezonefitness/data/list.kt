package com.example.risezonefitness.data

import com.example.risezonefitness.model.Admin
import com.example.risezonefitness.model.Member


val listMembers = mutableListOf(
    Member(
        "Ahmad Khaled",
        25,
        "Male",
        "0100010001",
        "ahmad1@mail.com",
        "HH123456",
        "ahmad1",
        "pass1"
    ),
    Member(
        "Omar Youssef",
        28,
        "Male",
        "0100010002",
        "omar2@mail.com",
        "HH123457",
        "omar2",
        "pass2"
    ),
    Member(
        "Youssef Adel",
        30,
        "Male",
        "0100010003",
        "youssef3@mail.com",
        "HH123458",
        "youssef3",
        "pass3"
    ),
    Member(
        "Mohamed Nabil",
        26,
        "Male",
        "0100010004",
        "mohamed4@mail.com",
        "HH123459",
        "mohamed4",
        "pass4"
    ),
    Member(
        "Khaled Hany",
        29,
        "Male",
        "0100010005",
        "khaled5@mail.com",
        "HH123460",
        "khaled5",
        "pass5"
    ),
    Member(
        "Hassan Fathy",
        24,
        "Male",
        "0100010006",
        "hassan6@mail.com",
        "HH123461",
        "hassan6",
        "pass6"
    ),
    Member(
        "Mostafa Ahmed",
        31,
        "Male",
        "0100010007",
        "mostafa7@mail.com",
        "HH123462",
        "mostafa7",
        "pass7"
    ),
    Member("Tarek Amr", 27, "Male", "0100010008", "tarek8@mail.com", "HH123463", "tarek8", "pass8"),
    Member(
        "Nader Sami",
        32,
        "Male",
        "0100010009",
        "nader9@mail.com",
        "HH123464",
        "nader9",
        "pass9"
    ),
    Member(
        "Islam Gamal",
        28,
        "Male",
        "0100010010",
        "islam10@mail.com",
        "HH123465",
        "islam10",
        "pass10"
    ),
    Member(
        "Karim Osama",
        26,
        "Male",
        "0100010011",
        "karim11@mail.com",
        "HH123466",
        "karim11",
        "pass11"
    ),
    Member(
        "Sherif Hossam",
        29,
        "Male",
        "0100010012",
        "sherif12@mail.com",
        "HH123467",
        "sherif12",
        "pass12"
    ),
    Member(
        "Fadi Magdy",
        27,
        "Male",
        "0100010013",
        "fadi13@mail.com",
        "HH123468",
        "fadi13",
        "pass13"
    ),
    Member(
        "Hesham Wael",
        30,
        "Male",
        "0100010014",
        "hesham14@mail.com",
        "HH123469",
        "hesham14",
        "pass14"
    ),
    Member(
        "Bassel Nabil",
        33,
        "Male",
        "0100010015",
        "bassel15@mail.com",
        "HH123470",
        "bassel15",
        "pass15"
    ),
    Member(
        "Ziad Hamdy",
        25,
        "Male",
        "0100010016",
        "ziad16@mail.com",
        "HH123471",
        "ziad16",
        "pass16"
    ),
    Member(
        "Mahmoud Tamer",
        28,
        "Male",
        "0100010017",
        "mahmoud17@mail.com",
        "HH123472",
        "mahmoud17",
        "pass17"
    ),
    Member("Ali Hatem", 31, "Male", "0100010018", "ali18@mail.com", "HH123473", "ali18", "pass18"),
    Member(
        "Ramy Said",
        26,
        "Male",
        "0100010019",
        "ramy19@mail.com",
        "HH123474",
        "ramy19",
        "pass19"
    ),
    Member(
        "Belal Fadel",
        29,
        "Male",
        "0100010020",
        "belal20@mail.com",
        "HH123475",
        "belal20",
        "pass20"
    )

        )

    val unpaidMembersList = mutableListOf<Member>()




    val listAdmins = mutableListOf(
        Admin("Admin", "admin", "admin")
    )

    val entryLogList = mutableListOf<String>()

    val memberAttendanceDays = mutableMapOf<String, MutableSet<String>>()





