# 1. Git 기본 개념
----
# 원격 저장소와 로컬 저장소
- 원격 저장소(Remote Repository): 여러 사람이 함께 공유하기 위한 저장소
- 로컬 저장소(Local Repository): 내 pc에 저장되는 개인 전용 저장소 

# 명령어
- git branch: 브랜치 확인
- git branch -D {브랜치 이름}: 브랜치 삭제
- git branch -r: 전체 브랜치 확인(원격 + 로컬)
- git clone {repository 주소}: 원격 저장소에 있는 프로잭트를 로컬 저장소로 이동
- git remote update: 원격 저장소에 업데이트 및 커밋 확인

## Pull: 원격 저장소에 있는 브랜치를 로컬 저장소로 가져옴
- git pull origin {브랜치 이름}

## Push: 로컬 저장소에 있는 코드를 원격 저장소에 적용
- git add .
- git commit -m "{commit 내용}"
- git push origin {브랜치 이름}

## Checkout
- git checkout {브랜치 이름}: 브랜치 이름
- git checkout -b {브랜치 이름}: 브랜치 생성 후 브랜치 변경
- git checkout -t {브랜치 이름}: 원격 저장소에 있는 브랜치를 로컬 저장소로 가져오고 브랜치 변경

<br><br>

# Pull request 절차
## 1. Clone
- git clone {저장소 주소} (브랜치 Code 누르고 링크 복사 ex)https://github.com/Hi-bike/Hi-Bike.git)

## 2. 로컬에 Branch 생성
- git checkout -b {안녕자전거} (git branch 명령어로 생성됐는지 확인)

## 3. 작업 후 add, commit, push
- git add .
- git commit -m "{개발사항, 수정사항 요약}" ex) git commit -m "feature/유저-로그인-기능-추가"
- git push origin {안녕자전거} (원격 저장소에 "안녕자전거"라는 브랜치 생성됨)
- 수정 사항 생기면 3번 반복

## 4. Pull Request(PR) 생성
- 작업 완료 후 Pull Request 생성(내가 만든 브랜치를 main브랜치에 적용)
- push 완료 후 본인 계정의 github 저장소에 들어오면 Compare & pull request 버튼이 활성화 되어 있음
- pr 올리면 코드 리뷰하고 merge하면 됨


<br><br>

# 원격 브랜치 작업 절차
## 브랜치 확인
- git branch -r: 전체 브랜치 확인(원격 + 로컬)(나가기: q)

## 원격 브랜치 이동
- git checkout -t {foo}: 원격에 있는 브랜치 로컬로 가져오고 브랜치 이동
## 로컬 브랜치 최신화
- git remote update: 업데이트 된 사항 확인
- git pull origin {foo}: 원격 저장소에 있는 브랜치의 업데이트 내용을 로컬 브랜치에 적용(최신화)
## 이후
- 이후 절차는 Pull request 절차 3번 부터 같음