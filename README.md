<h1>
 <img src="logo.png" width="400" /> 
</h1>
<h6>
    Сферы и талисманы с эффектами
</h6>

### Сфера
> Предмет, представленный в виде головы игрока. Активирует определённые эффекты, когда находится в 40-м слоте инвентаря (Левая рука).

Выдача сферы:
>/givesphere [nick] [sphereName] [sphereType]
### Типы сферы
###### Каждая сфера имеет 3 комбинации эффектов. Каждая комбинация называется типом сферы. Каждая комбинация имеет в своих настройках отдельное название предмета, отдельный лор и список эффектов.

Игрок может улучшать сферу, меняя ее типы. Предполагается, что самый "слабый" тип - NORMAL, самый сильный - LEGEND.
Для получения более высокого типа сферы, игрок должен соединить в наковальне две сферы одного типа. В результате будет случайная сфера более высокого типа.
<center><img src="https://i.ibb.co/HVf0m6M/image.png"></center>

##### ИМЯ И ОПИСАНИЕ
Каждая сфера в своем стандартном виде (голова игрока) принимает настройки имени предмета и лора.
```yaml
display-name: "Магическая сфера"
lore:
  - "§fСфера даёт:"
  - ""
  - "§f+10% к скорости"
  - "§f+1 к броне"
  - ""
  - "§8Возьмите в левую руку"
```
##### ТЕКСТУРА СТАНДОРТНОГО ПРЕДСТАВЛЕНИЯ
Т.к. стандартное представление сферы - голова, есть способ указать идентификатор текстуры головы.
```yaml
texture: eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWU5NTI5M2FjYmNkNGY1NWZhZjU5NDdiZmM1MTM1MDM4YjI3NWE3YWI4MTA4NzM0MWI5ZWM2ZTQ1M2U4MzkifX19
```
##### ЭФФЕКТЫ
Каждый тип сферы может иметь набор эффектов.
> SPEED - Множитель скорости в процентах
> 
> DAMAGE - Количество единиц урона, добавляемых к необработанному урону во время нанесения урона
> 
> ARMOR - Количество единиц урона, отнимаемых от необработанного урона во время получения урона от Ентити
> 
> RUSH - Маяк в кармане с эффектом "Спешка". Не будет продлен, если игрок в ПВП. Значение эффекта - уровень спешки.

```yaml
effects:
  - type: SPEED
    value: 10
```
##### ПРЕДСТАВЛЕНИЕ В ВИДЕ ТАЛИСМАНА
Сфера  может быть представлена в виде тотема, внутри такое представление называется "Талисман".
Талисман имеет свое название и описание, однако эффекты наследуются от типа сферы. Для каждого типа свой талисман.
```yaml
mascot:
  enabled: true # Может ли игрок скрафтить талисман в наковальне
  display-name: "§aТалисман из сферы Stringer (Обычный)"
  lore:
    - "§fТалисман даёт:"
    - ""
    - "§f+10% к скорости"
    - ""
    - "§8Возьмите в левую руку"
```
Игрок может скрафтить талисман. Для этого требуется взять сферу и соединить с тотемом в наковальне

<center><img src="https://i.ibb.co/MMc3FCh/image.png"></center>

## API
Получение инстанса сферы по ее имени
```java
SphereManager manager = Mascots.getInstance().getSphereManager();
Sphere sphere = manager.getSphere("Stinger");
```

Получение инстанса сферы по объекту стопки предмета
```java
SphereManager manager = Mascots.getInstance().getSphereManager();
Sphere sphere = manager.getSphere(ItemStack);
```

Проверка стопки, является ли стопка сферой в любом ее проявлении?
```java
SphereManager manager = Mascots.getInstance().getSphereManager();
if (manager.isSphere(ItemStack)) {
    // code...    
}
```

Получение нового ItemStack сферы
```java
SphereManager manager = Mascots.getInstance().getSphereManager();
Sphere sphere = manager.getSphere("Stinger");
ItemStack sphereStack = sphere.getItemStack(SphereType.EPIC, false);
Player#openInventory(sphereStack);
```

Получение работающей в текущий момент сферы игрока, если она есть
```java
SphereManager manager = Mascots.getInstance().getSphereManager();
ItemSphere is = manager.getEffectHandler().getActiveSphere(Player).getSphere();
```
### Зависимости
[AntiRelog](https://github.com/AntiRelog/AntiRelog) <br>
[ProtocolLib](https://github.com/dmulloy2/ProtocolLib/)
###### by Matvei Slotvinskiy


