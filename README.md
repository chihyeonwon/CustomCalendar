# CustomCalendar
## 23.10.10
![image](https://github.com/chihyunwon/CustomCalendar/assets/58906858/43cf17ae-eea4-45b2-b729-a81d24682c1f)
```
선택한 날짜 데이터를 intent로 넘겨줌
```
#### 시스템 UI
![image](https://github.com/chihyunwon/CustomCalendar/assets/58906858/4a3d046a-6a75-4c33-9792-e0dd0a1886ee)          
![image](https://github.com/chihyunwon/CustomCalendar/assets/58906858/2d3568a0-40bb-4dda-aaba-c0a744275808)    

#### Drawerlayout에 ListView 넣기
![image](https://github.com/mr-won/CustomCalendar/assets/58906858/459e7476-bae5-4d1d-85cf-221f6102f8c5)
```
menu는 item태그만 쓸 수 있어서 MainActivity의 drawerlayout 안의 navigationView 안에 ListView를 넣어줘서 해결했다.
```
#### NavigationView에 LinearLayout 추가
![image](https://github.com/mr-won/CustomCalendar/assets/58906858/e793605d-42aa-4db4-a1ac-21ff9d0002ea)
```
메뉴를 통해서 액티비티를 썼던 것을 NavigationView 안에다가 그냥 다 넣어줬다.
```
#### 친구 추가 로직수정


#### Firebase Notification 사용하기

#### D-Day 구현
![image](https://github.com/mr-won/CustomCalendar/assets/58906858/2b9c3191-5108-4a72-ac51-0a3e6c964eb8)
```
파이어베이스에 입력된 startDate와 오늘 날짜 currentDate를 SimpleDateFormat 메서드를 사용하여 yyyy-MM-dd의 형태로 바꿔준뒤
parse메서드를 사용하여 시간으로 변경한 후 두 날짜의 차이를 시간으로 구한다. 그 후 나온 시간의 차이를 (1000 * 60 * 60 * 24) 날짜의 형태로
수정한 후 diffInDays 변수에 저장한다. diffInDays가 음수일 때는 절댓값을 취한 뒤 + 기호를 붙여서 출력하고 양수일 때는 - 기호만 붙여서 출력한다.
```

#### D-Day Sort
![image](https://github.com/mr-won/CustomCalendar/assets/58906858/6b01f437-2b98-4e4d-b233-ab8ade1aa083)
```
startDate 기준으로 정렬하기 위해서 데이터를 가져오는 부분에 notiList.sortBy { it.startDate }를 추가했다.
startDate 기준으로 빠른 것부터 정렬되서 데이터를 가져올 수 있다.
```
#### plan 데이터를 넣고 뺄 때 uid를 사용해서 데이터베이스의 경로를 수정한다음 구분해줘야함
