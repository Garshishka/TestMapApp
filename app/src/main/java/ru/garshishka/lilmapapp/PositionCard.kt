package ru.garshishka.lilmapapp

data class PositionCard(
    val name: String,
    val description: String,
    val pic: Int,
)

val cards = listOf(
    PositionCard(
        "Позиция №1",
        "Описание позиции №1, здесь находится описание позиции номер 1.",
        R.drawable.position_one_pic_48
    ),
    PositionCard(
        "Позиция Два",
        "Здесь находится описание позиции номер 2. Описание позиции №2.",
        R.drawable.position_two_pic_48
    ),
    PositionCard(
        "Третья позиция",
        "Описание позиции №3, описание позиции №3, описание позиции №3, описание позиции №3.",
        R.drawable.position_three_pic_48
    ),
    PositionCard(
        "Позиция IV",
        "Здесь располагается текст описания позиции номер 4.",
        R.drawable.position_four_pic_48
    )
)
