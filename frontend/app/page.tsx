'use client'

import { useState } from 'react'
import { SignupCard } from '@/components/signup-card'

const heroImage = 'https://hebbkx1anhila5yf.public.blob.vercel-storage.com/image-2ofQaP2g589ZDliECXbT0xHDtm8I1F.png'

export default function Page() {
  const [view, setView] = useState<'home' | 'login' | 'signup'>('home')

  return (
    <main className="site-shell">
      <header className="top-header">
        <div className="brand-row">
          <button className="brand" onClick={() => setView('home')} aria-label="우리집 인테리어 홈">
            <span className="brand-mark" />
            <strong>우리집 인테리어</strong>
          </button>
          <nav className="primary-nav" aria-label="주요 메뉴">
            <a className="active" href="#home">집구경</a>
            <a href="#shopping">쇼핑</a>
            <a href="#lifestyle">인테리어/생활</a>
          </nav>
          <div className="header-actions">
            <label className="search-box">
              <span className="sr-only">검색</span>
              <input placeholder="통합검색" />
            </label>
            <button className="text-button" onClick={() => setView('login')}>로그인</button>
            <span className="divider" aria-hidden="true" />
            <button className="text-button" onClick={() => setView('signup')}>회원가입</button>
            <span className="divider" aria-hidden="true" />
            <button className="text-button">고객센터</button>
            <button className="write-button">내 방 등록하기</button>
          </div>
        </div>
        <div className="category-row">
          <nav aria-label="콘텐츠 메뉴">
            <a className="home-link" href="#home">홈</a>
            <a href="#recommend">추천</a>
            <a href="#homes">집들이</a>
            <a href="#magazine">집사진</a>
            <a href="#community">커뮤니티</a>
            <a href="#3d">3D인테리어</a>
          </nav>
          <span className="scrap-count">7&nbsp; 작은방꾸미기</span>
        </div>
      </header>

      <section className="content" id="home" aria-label="오늘의 집들이">
        {view === 'signup' ? (
          <SignupCard onLogin={() => setView('login')} />
        ) : view === 'login' ? (
          <div className="login-card">
            <h1>우리집 인테리어에 오신 걸 환영해요</h1>
            <p>나만의 공간을 더 즐겁게 기록해보세요.</p>
            <form onSubmit={(event) => event.preventDefault()}>
              <label htmlFor="email">이메일</label>
              <input id="email" type="email" placeholder="이메일 주소를 입력해주세요" />
              <label htmlFor="password">비밀번호</label>
              <input id="password" type="password" placeholder="비밀번호 입력은 기능 준비 중이에요" />
              <button className="login-button" type="submit">로그인</button>
              <div className="login-links">
                <button className="login-link" type="button">비밀번호 재설정</button>
                <span className="login-links-divider" aria-hidden="true" />
                <button className="login-link" type="button" onClick={() => setView('signup')}>회원가입</button>
              </div>
              <button className="social-button kakao-button" type="button"><span className="social-icon kakao-icon" aria-hidden="true"><svg viewBox="0 0 36 32" width="26" height="23" focusable="false"><path d="M18 1C8.6 1 1 7.2 1 14.8c0 4.9 3.2 9.2 8 11.6l-1.9 6.4a.6.6 0 0 0 .9.7l7.6-5c1.4.2 2.9.3 4.4.3 9.4 0 17-6.2 17-13.8S27.400 1 18 1Z" fill="#3C1E1E" /><text x="18" y="17.600" textAnchor="middle" fontFamily="Arial, Helvetica, sans-serif" fontWeight="900" fontSize="9.500" letterSpacing="-0.300" fill="#FEE500">TALK</text></svg></span>카카오톡으로 로그인하기</button>
              <button className="social-button naver-button" type="button"><span className="social-icon naver-icon" aria-hidden="true">N</span>네이버로 로그인하기</button>
              <button className="social-button facebook-button" type="button"><span className="social-icon facebook-icon" aria-hidden="true">f</span>페이스북으로 로그인하기</button>
            </form>
            <small>로그인 기능은 준비 중입니다.</small>
          </div>
        ) : (
          <div className="feature-grid">
            <article className="feature-card">
              <div className="room-scene" role="img" aria-label="햇살이 들어오는 책장과 책상, 침실이 있는 따뜻한 방">
                <div className="window-light" />
                <div className="wall-shadow" />
                <div className="shelf">
                  <span /><span /><span /><span /><span /><span /><span /><span /><span /><span /><span /><span />
                </div>
                <div className="desk"><i /><i /><i /></div>
                <div className="chair" />
                <div className="bed" />
                <div className="floor-light" />
                <div className="feature-copy">
                  <h1>낡은 종이와 책, 패브릭을 좋아하는 자취생의 박물관 같은 집</h1>
                  <p>인생은아슬아슬</p>
                </div>
              </div>
            </article>
            <aside className="promo-card">
              <span>이벤트</span>
              <p>행운출첵 참여하면</p>
              <strong>포인트 무조건 지급!</strong>
              <div className="coupon"><small>최대</small><b>20,000원</b></div>
              <em>10/17 ~</em>
            </aside>
          </div>
        )}
      </section>
    </main>
  )
}
