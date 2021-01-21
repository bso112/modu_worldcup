# 모두의 월드컵
다양한 주제별 이상형 월드컵을 진행하는 앱입니다.<br/>
기존의 이상형 월드컵과 달리, 다수의 사용자가 하나의 이상형월드컵을 만들어 나갈 수 있습니다

## 사용기술
Kotlin, Android Studio, MySQL, Node.js

## 실행법
```
# 깃 허브에서 소스코드를 다운로드 받습니다.
git clone https://github.com/bsw112/anime_worldcup.git

# 안드로이드 스튜디오로 빌드합니다. 개발환경은 4.1 버전입니다.
```


## 주요기능
* FirbaseAuth를 이용한 구글로그인. JWT 토큰으로 로그인을 유지합니다.
* 이상형월드컵 CRUD
* 이상형월드컵의 후보들이 부족하다고 느낀다면, 타 사용자가 사진을 추가 할 수 있습니다.
  예) 가장 강한 캐릭터를 뽑는 월드컵인데 손오공이라는 캐릭터가 없는게 불만인 사용자가 직접 이상형월드컵에 사진을 추가할 수 있습니다.
* 이상형월드컵에 올린 사진들에 대한 CRUD
* 댓글, 대댓글의 CRUD
* Firebase를 이용한 FCM, 알림 클릭시 pending intent를 이용해 알림의 대상으로 바로 이동합니다.
* 포인트제도가 있어, 다른 사람이 내 월드컵을 완료시 혹은 내가 올린 사진이 월드컵에서 우승했을시 포인트를 얻을 수 있습니다.
* 다크모드, 영어지원


## 실제화면
![alt](readme/슬라이드5.PNG)
![alt](readme/슬라이드6.PNG)
![alt](readme/슬라이드7.PNG)
![alt](readme/슬라이드8.PNG)
![alt](readme/슬라이드9.PNG)
![alt](readme/슬라이드10.PNG)
![alt](readme/슬라이드11.PNG)
![alt](readme/슬라이드12.PNG)

## 시연
[![시연](http://img.youtube.com/vi/n00snXrRC84/0.jpg)](https://www.youtube.com/watch?v=n00snXrRC84)


