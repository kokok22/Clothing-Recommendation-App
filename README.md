## 사용자 기반 패션 추천 어플

### 1. 작동
1) 사용자가 본인의 옷을 촬영하여 어플에 저장
2) 사용자의 옷 조합이나 옷 스타일이 유사한 다른 사용자 또는 패션 인플루언서를 추천


### 2. 구현
1) 의류분석 알고리즘을 통해 입력된 의류 데이터의 라벨링
2) 패션 인플루언서들의 스타일을 라벨링(댄디, 아메카지 등등..)하여 딥러닝 모델에 학습
3) 학습된 모델에 사용자의 의류 데이터를 넣어 스타일 결정
