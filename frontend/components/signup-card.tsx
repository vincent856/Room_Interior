'use client'

import { useState } from 'react'

const EMAIL_DOMAINS = ['naver.com', 'gmail.com', 'daum.net', 'kakao.com', 'hanmail.net', 'nate.com']
const CUSTOM_DOMAIN = '직접 입력'

type Agreements = {
  age: boolean
  terms: boolean
  privacy: boolean
  marketing: boolean
}

const agreementItems: { key: keyof Agreements; label: string; required: boolean }[] = [
  { key: 'age', label: '만 14세 이상입니다', required: true },
  { key: 'terms', label: '이용약관', required: true },
  { key: 'privacy', label: '개인정보수집 및 이용 동의', required: true },
  { key: 'marketing', label: '마케팅 정보 수신 동의 (이벤트/혜택 등)', required: false },
]

function validatePassword(value: string) {
  if (value.length < 8) return '8자 이상 입력해주세요.'
  if (!/[A-Za-z]/.test(value) || !/\d/.test(value)) return '영문, 숫자를 포함해주세요.'
  return ''
}

function FacebookIcon() {
  return (
    <svg viewBox="0 0 24 24" width="30" height="30" aria-hidden="true" focusable="false">
      <path
        d="M13.5 21v-7.5h2.6l.5-3.1h-3.1V8.5c0-.9.4-1.6 1.7-1.6h1.5V4.2c-.3 0-1.2-.2-2.3-.2-2.4 0-4 1.4-4 4.1v2.3H7.8v3.1h2.6V21h3.1Z"
        fill="#fff"
      />
    </svg>
  )
}

function KakaoIcon() {
  return (
    <svg viewBox="0 0 36 32" width="36" height="32" aria-hidden="true" focusable="false">
      <path
        d="M18 1C8.6 1 1 7.2 1 14.8c0 4.9 3.2 9.2 8 11.6l-1.9 6.4a.6.6 0 0 0 .9.7l7.6-5c1.4.2 2.9.3 4.4.3 9.4 0 17-6.2 17-13.8S27.400 1 18 1Z"
        fill="#3C1E1E"
      />
      <text
        x="18"
        y="17.600"
        textAnchor="middle"
        fontFamily="Arial, Helvetica, sans-serif"
        fontWeight="900"
        fontSize="9.500"
        letterSpacing="-0.300"
        fill="#FEE500"
      >
        TALK
      </text>
    </svg>
  )
}

function NaverIcon() {
  return (
    <svg viewBox="0 0 20 20" width="24" height="24" aria-hidden="true" focusable="false">
      <path d="M13.560 10.700 6.200 0H0v20h6.440V9.300L13.800 20H20V0h-6.440v10.700Z" fill="#fff" />
    </svg>
  )
}

export function SignupCard({ onLogin }: { onLogin: () => void }) {
  const [emailId, setEmailId] = useState('')
  const [domain, setDomain] = useState('')
  const [customDomain, setCustomDomain] = useState('')
  const [password, setPassword] = useState('')
  const [passwordConfirm, setPasswordConfirm] = useState('')
  const [nickname, setNickname] = useState('')
  const [agreements, setAgreements] = useState<Agreements>({
    age: false,
    terms: false,
    privacy: false,
    marketing: false,
  })
  const [submitted, setSubmitted] = useState(false)
  const [verifyNotice, setVerifyNotice] = useState(false)
  const [done, setDone] = useState(false)

  const isCustom = domain === CUSTOM_DOMAIN
  const allChecked = Object.values(agreements).every(Boolean)
  const emailReady = !!emailId.trim() && !!domain && (!isCustom || !!customDomain.trim())

  const errors = {
    email: emailReady ? '' : '이메일을 입력하고 도메인을 선택해주세요.',
    password: validatePassword(password),
    passwordConfirm: password !== passwordConfirm || !passwordConfirm ? '비밀번호가 일치하지 않습니다.' : '',
    nickname: nickname.trim().length < 2 || nickname.trim().length > 15 ? '2~15자로 입력해주세요.' : '',
    terms: agreements.age && agreements.terms && agreements.privacy ? '' : '필수 약관에 동의해주세요.',
  }
  const hasError = Object.values(errors).some(Boolean)

  const toggleAll = (checked: boolean) =>
    setAgreements({ age: checked, terms: checked, privacy: checked, marketing: checked })

  const handleSubmit = (event: React.FormEvent) => {
    event.preventDefault()
    setSubmitted(true)
    if (!hasError) setDone(true)
  }

  const fieldError = (message: string) =>
    submitted && message ? (
      <p className="signup-error" role="alert">
        {message}
      </p>
    ) : null

  return (
    <div className="signup-card">
      <h1>회원가입</h1>

      <section className="signup-sns" aria-label="SNS 계정으로 회원가입">
        <p>SNS계정으로 간편하게 회원가입</p>
        <div className="signup-sns-list">
          <button type="button" className="signup-sns-button facebook" aria-label="페이스북으로 회원가입">
            <FacebookIcon />
          </button>
          <button type="button" className="signup-sns-button kakao" aria-label="카카오톡으로 회원가입">
            <KakaoIcon />
          </button>
          <button type="button" className="signup-sns-button naver" aria-label="네이버로 회원가입">
            <NaverIcon />
          </button>
        </div>
      </section>

      <form onSubmit={handleSubmit} noValidate>
        <div className="signup-field">
          <label htmlFor="signup-email-id">이메일</label>
          <div className="signup-email-row">
            <input
              id="signup-email-id"
              type="text"
              placeholder="이메일"
              autoComplete="username"
              value={emailId}
              onChange={(event) => setEmailId(event.target.value)}
              aria-invalid={submitted && !!errors.email}
            />
            <span className="signup-at" aria-hidden="true">@</span>
            <select
              aria-label="이메일 도메인 선택"
              className={domain ? '' : 'is-empty'}
              value={domain}
              onChange={(event) => {
                setDomain(event.target.value)
                setVerifyNotice(false)
              }}
              aria-invalid={submitted && !domain}
            >
              <option value="" disabled>선택해주세요</option>
              {EMAIL_DOMAINS.map((item) => (
                <option key={item} value={item}>{item}</option>
              ))}
              <option value={CUSTOM_DOMAIN}>{CUSTOM_DOMAIN}</option>
            </select>
          </div>
          {isCustom ? (
            <input
              type="text"
              aria-label="이메일 도메인 직접 입력"
              placeholder="도메인을 입력해주세요 (예: example.com)"
              value={customDomain}
              onChange={(event) => setCustomDomain(event.target.value)}
              aria-invalid={submitted && !customDomain.trim()}
            />
          ) : null}
          <button
            type="button"
            className="signup-verify"
            disabled={!emailReady}
            onClick={() => setVerifyNotice(true)}
          >
            이메일 인증하기
          </button>
          {verifyNotice ? <p className="signup-hint">이메일 인증 기능은 준비 중입니다.</p> : null}
          {fieldError(errors.email)}
        </div>

        <div className="signup-field">
          <label htmlFor="signup-password">비밀번호</label>
          <p className="signup-desc">영문, 숫자를 포함한 8자 이상의 비밀번호를 입력해주세요.</p>
          <input
            id="signup-password"
            type="password"
            placeholder="비밀번호"
            autoComplete="new-password"
            value={password}
            onChange={(event) => setPassword(event.target.value)}
            aria-invalid={submitted && !!errors.password}
          />
          {fieldError(errors.password)}
        </div>

        <div className="signup-field">
          <label htmlFor="signup-password-confirm">비밀번호 확인</label>
          <input
            id="signup-password-confirm"
            type="password"
            placeholder="비밀번호 확인"
            autoComplete="new-password"
            value={passwordConfirm}
            onChange={(event) => setPasswordConfirm(event.target.value)}
            aria-invalid={submitted && !!errors.passwordConfirm}
          />
          {fieldError(errors.passwordConfirm)}
        </div>

        <div className="signup-field">
          <label htmlFor="signup-nickname">닉네임</label>
          <p className="signup-desc">다른 유저와 겹치지 않는 별명을 입력해주세요. (2~15자)</p>
          <input
            id="signup-nickname"
            type="text"
            placeholder="별명 (2~15자)"
            autoComplete="nickname"
            value={nickname}
            onChange={(event) => setNickname(event.target.value)}
            aria-invalid={submitted && !!errors.nickname}
          />
          {fieldError(errors.nickname)}
        </div>

        <fieldset className="signup-terms">
          <legend>약관동의</legend>
          <label className="signup-check signup-check-all">
            <input type="checkbox" checked={allChecked} onChange={(event) => toggleAll(event.target.checked)} />
            <span>전체동의</span>
          </label>
          <p className="signup-terms-note">선택 항목에 동의하지 않아도 서비스 이용이 가능합니다.</p>
          <div className="signup-terms-list">
            {agreementItems.map((item) => (
              <label className="signup-check" key={item.key}>
                <input
                  type="checkbox"
                  checked={agreements[item.key]}
                  onChange={(event) => setAgreements((prev) => ({ ...prev, [item.key]: event.target.checked }))}
                />
                <span>
                  <em className={item.required ? 'required' : 'optional'}>{item.required ? '필수' : '선택'}</em>
                  {item.label}
                </span>
              </label>
            ))}
          </div>
          {fieldError(errors.terms)}
        </fieldset>

        <button className="signup-submit" type="submit">회원가입하기</button>
        {done ? (
          <p className="signup-done" role="status">입력하신 정보를 확인했어요. 회원가입 기능은 준비 중입니다.</p>
        ) : null}
      </form>

      <p className="signup-login-link">
        이미 아이디가 있으신가요?
        <button type="button" onClick={onLogin}>로그인</button>
      </p>
    </div>
  )
}
