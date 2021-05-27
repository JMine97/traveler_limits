# traveler_limits (항공권 지정가 알림 서비스)
사용자가 지정한 가격 이하의 항공권이 나오면 알람을 제공합니다<br><br>
&nbsp;개인 맞춤형 알람 기능을 제공해 사용자의 항공권 가격 변동 모니터링 노력을 줄여주고 
합리적인 가격에 효율적인 소비를 할 수 있도록 돕는 것이 목적입니다.<br><br>
&nbsp;주식 시장에서 시장 가격이 지정된 가격에 도달하거나 더 유리한 가격일 때 거래가 체결되는 지정가 거래 주문 방법을 적용했습니다. <br><br>

## 목차
* [사용 기술](#1-사용-기술)
* [주요 기능](#2-주요-기능)
* [역할 분배](#3-역할-분배)<br><br>



## 1. 사용 기술
<img src="https://user-images.githubusercontent.com/52027965/119838674-90042380-bf3e-11eb-9af5-c25d7843b5e1.png" width="60%" height="50%"><br><br>
서비스 제공을 위해 작동되는 작업의 플로우차트입니다.   

사용자는 안드로이드 기반의 기기를 사용해 정보를 조회하면 api를 통해 데이터를 제공하고   
저장된 모든 정보는 데이터베이스에서 관리 운영합니다.   

알람은 서버에서 firebase를 통해 제공하며 서버는 aws ec2를 사용해 구축했습니다.  <br><br>


## 2. 주요 기능
![image](https://user-images.githubusercontent.com/52027965/119842714-e2930f00-bf41-11eb-83f9-662cf7f55210.png)<br><br>
회원 가입 시 개인 DB를 생성하여 개인 정보, 검색 기록, 알람 목록 등을 관리합니다. 
자동 로그인, 회원정보 수정 및 회원탈퇴 기능 등을 제공합니다. <br><br><br>

![image](https://user-images.githubusercontent.com/52027965/119843000-1bcb7f00-bf42-11eb-9bfb-c745ffcccf13.png)<br>

![image](https://user-images.githubusercontent.com/52027965/119843117-33a30300-bf42-11eb-9c0c-f79bd9f30c51.png)<br><br>
실시간으로 조회된 항공권의 항공사, 경유 정보, 비행 시간과 가격을 제공합니다.
항공권의 가격 정보를 바탕으로 가격 분포도를 그려 보여줍니다. 
사용자는 해당 분포도를 참고햐여 합리적인 가격 정보를 얻고, 알람을 예약 할 지정 가격을 설정하는데 도움을 받을 수 있습니다.<br><br><br>

![image](https://user-images.githubusercontent.com/52027965/119843336-6b11af80-bf42-11eb-9aaf-3886347c3d55.png)<br><br>
사용자가 지정 가격 알람을 저장하면 웹 서버에서 해당 항공권 가격 정보를 주기적으로 모니터링합니다. 
지정 가격의 항공권을 발견하면 정확한 사용자에게 알람 서비스를 제공합니다.<br><br>

## 3. 역할 분배
* 주혜경 : 기획, Android Studio, UI/UX, 프로젝트 관리
* 안예린 : Firebase, Amadeus API, 안정화&최적화, Android Studio
* 최정민 : AWS 서버구축, php, Data Base, Android Studio
