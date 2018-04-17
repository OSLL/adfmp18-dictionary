# adfmp18-dictionary

Maket: https://ninjamock.com/s/BRSFQRx

### UML

![alt_text](https://github.com/OSLL/adfmp18-dictionary/blob/master/uml/1.png)
![alt_text](https://github.com/OSLL/adfmp18-dictionary/blob/master/uml/2.png)

### Performance words list

![1](https://github.com/OSLL/adfmp18-dictionary/blob/master/perf_1.png)
![1001](https://github.com/OSLL/adfmp18-dictionary/blob/master/perf_1000.png)
![10001](https://github.com/OSLL/adfmp18-dictionary/blob/master/perf_10000.png)
![100001](https://github.com/OSLL/adfmp18-dictionary/blob/master/perf_100000.png)

We conclude that the performance is independent of the number of elements


### Оценка UI

#### Добавление слова

* Выделение слова
* Выбрать "share"
* Выбрать "dictionary"
* Нажать "save"

##### Итого: 4 клика, 1 выделение

#### Удаление слова

* Выбрать слово
* Выбрать "удалить"

##### Итого: 2 клика
##### Как улучшить: свайп вправо: удаление. Итого: 1 свайп

#### Перенос слова в категорию "на изучение/изученные"

* Выбрать слово
* Выбрать "изучать/изучено" соответственно

##### Итого: 2 клика
##### Как улучшить: свайп влево: перенос слова. Итого: 1 свайп

#### Переход к тестированию

* Выбрать выпадающий список
* Выбрать "тест"
* Максимум 10 слов, значит 10 кликов по ">"
* Нажать "start"
* Для каждого слово ввести его перевод, нажать "check" и ">"

##### Итого: 33 клика + (средняя длина слова в русском языке 7 символов) * 10 = 103 клика
##### Как улучшить: Убрать выпадающий список; Вместо ввода перевода слова - 4 варианта ответа; автоматический переход между словами. Итого: 22 клика
