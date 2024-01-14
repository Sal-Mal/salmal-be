Feature: 살말 인수 테스트 -
  파이어베이스 인증과정을 통해 생성된 사용자의 'providerId' 를 이용해 로그인 후 투표 생성 완료되고 사용자가 생성한 투표가 조회 되어야 한다.

  Scenario Outline: 파이어베이스 인증과정을 통해 제공받은 providerId 통한 로그인 과정
    Given 파이어베이스 인증후 요청 시 "<providerId>" 가 주어진다.
    When /api/auth/login 을 통해 요청 한다.
    Then providerId 가 유효한 id 인지 검증하고 생성된 인증토큰과 재발급 토큰을 받는다.
    Examples: 테스트용 providerId
      | providerId                         |
      | 31dashjk123jhkdasjkh23413214ncjlxz |


