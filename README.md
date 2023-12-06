# TestMapApp

Приложение, имитирующее работу с картой.

## Экраны

### 1. Основной экран
   
Карта из OpenStreetMaps. Нажатие на карту создает на карте точку. При нажатии в другом месте карты точка перемещается туда.

Кнопка "Точку в центр" центрирует карту на установленной точке.

Кнопка "Фокус" включает режим, при котором нажатие на карту создает вторую точку, после чего между двумя точками рисуется прямая линия. При нажатии в другом месте карты вторая точка перемещается туда.

Кнопка меню открывает экран с меню.

### 2. Меню

Содержит кнопки, которые перемещают на экран с категориями
  
### 3. Категории

Содержит кнопки, которые перемещают на экран карточки
   
### 4. Карточка позиции

В зависимости от выбранной позиции на данном экране отображается выбранная позиция: ее названия, описание, изображение.

## Техническая информация
 
Информация о позициях (название, описание, изображение) хранится в [дата классе](https://github.com/Garshishka/TestMapApp/blob/master/app/src/main/java/ru/garshishka/lilmapapp/PositionCard.kt). Также там находится список из 4 заранее созданных позиций.

Всего в приложении имеется 5 фрагментов:
1. [Фрагмент карты](https://github.com/Garshishka/TestMapApp/blob/master/app/src/main/java/ru/garshishka/lilmapapp/fragment/MapFragment.kt)
2. [Фрагмент меню](https://github.com/Garshishka/TestMapApp/blob/master/app/src/main/java/ru/garshishka/lilmapapp/fragment/MenuFragment.kt)
3. [Фрагмент категории 1](https://github.com/Garshishka/TestMapApp/blob/master/app/src/main/java/ru/garshishka/lilmapapp/fragment/CategoryOneFragment.kt)
4. [Фрагмент категории 2](https://github.com/Garshishka/TestMapApp/blob/master/app/src/main/java/ru/garshishka/lilmapapp/fragment/CategoryTwoFragment.kt)
5. [Фрагмент карточки позиции](https://github.com/Garshishka/TestMapApp/blob/master/app/src/main/java/ru/garshishka/lilmapapp/fragment/PositionCardFragment.kt)

Выбранная позиция передается с 3его и 4его фрагмента на 5ый через Bundle.

[Навигация фрагментов](https://github.com/Garshishka/TestMapApp/blob/master/app/src/main/res/navigation/nav_main.xml)
