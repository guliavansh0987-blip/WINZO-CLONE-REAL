# GameZone Feature Gap Analysis & Implementation Plan

## 1. Executive Summary & Code-Level Audit
Based on the code inspection and the 8 visual reference screenshots provided:
- **Reference 1 (`IMG_85bad1fa...`)**: Two-team Guild Clash / World War animated scoreboard. Left team in vibrant blue gradient with glowing rising blue tower (401 Avg, 62,983 Total, 150+ members), Right team in golden/orange gradient with glowing rising tower (336 Avg, 41,370 Total, 100+ members), glowing "AHEAD BY 65" / "BEHIND BY" badge, user personal score breakdown (`4336`), and dynamic confetti / victory state.
- **Reference 2 (`Screenshot_..._088618ee`)**: Transactions Screen with purple header, 3-tab filter pills (`All`, `Tds Transactions`, `Add Cash/Withdrawal`), "You can only view transactions for the last 30 days" notice card, transaction cards with GST breakdown, date/time, and "+₹100 / Help" action.
- **Reference 3 (`Screenshot_..._60cf03bc`)**: Lobby screen with deep purple/wine background, custom top header (Avatar, Diamonds `90`, Wallet `$2.14`, Notification bell, Spin wheel icon), "Trending", "For You" (Solitaire, Domino, Mines), "Popular on WinZO" (Ludo #1, Spades #2, Ludo No. 1 #3, Rummy #4), Referral promotion card, and 5-item bottom bar.
- **Reference 4 (`Screenshot_..._ad10c823`)**: 3-Tier Wallet layout with Unplayed/Deposit, Bonus, Winnings, green "Deposit" button, "Earn Bonus", "Withdraw" button, and 100% Cashback banner.
- **Reference 5 (`Screenshot_..._b380c2c2`)**: Fantasy Cricket Captain & Vice-Captain selection modal (`(C) gets 2x points, (VC) gets 1.5x points`) with player headshots, team badges, Wicket-Keeper, Batsmen, All-Rounders, Bowlers, "Team Preview" and purple "Save Team" button.
- **Reference 6 (`Screenshot_..._41af9629`)**: World War competition cards with "2 Teams / 4 Teams", "Highscore Counts / All Scores Count", live countdown timer (`03:41`, `39:41`), team details button, and entry chip buttons.
- **Reference 7 (`Screenshot_..._f45b8151`)**: Game category grid with colorful high-contrast badge tiles: Knife Up, Bubble Shooter, Candy Match, Carrom, Cricket, Space Warrior, Fruit Samurai, Memory Mania.

---

## 2. Feature Audit Status

| Feature Area | Current Status | Audit Details & Gap | Target Action |
|---|---|---|---|
| **Guild Clash Scoreboard** | PARTIALLY WORKING | Had basic card dialog; missing the animated dual towers, split-screen background, ahead-by badges, rising tower physics, and live score difference | **Implement full animated split tower scoreboard (Reference 1)** |
| **Guild Clash Lobby** | PARTIALLY WORKING | Generic cards; missing authentic 2-Team/4-Team tags, rule tags (Highscore Counts vs All Scores Count), and direct scoreboard link | **Upgrade to match Reference 6** |
| **Game Lobby & Categories** | PARTIALLY WORKING | Had 6 games; missing Candy Match, Carrom, Fruit Samurai, Memory Mania, Space Warrior, and exact visual tile art | **Add game modules & vibrant tile assets (Reference 7 & 3)** |
| **Fantasy Captain/Vice-Captain** | PARTIALLY WORKING | Had squad picker; missing dedicated C (2x) & VC (1.5x) player list screen with headshots & previews | **Implement Captain/VC selection flow (Reference 5)** |
| **Transactions History** | PARTIALLY WORKING | Basic list; missing 3-pill filter (All, TDS, Add Cash/Withdrawal), GST breakdown tags, and 30-day banner | **Align 1:1 with Reference 2** |
| **Wallet 3-Tier** | WORKING | 3-tier balances already present | **Polish exact colors & banner from Reference 4** |
| **Daily Rewards & Streaks** | WORKING | Daily spin wheel, scratch cards, and 10-day streak | **Retain and link smoothly into top header** |
| **Backend / User Architecture** | WORKING | Client-side reactive repository with StateFlows; user answered "Create user only for now, admin later" | **Solidify user engine, state models, and score calculation** |

---

## 3. Step-by-Step Implementation Strategy

### Phase 1: Visual Identity & Color Theme
- Update `Color.kt` and `Theme.kt` to reproduce the exact palette in the screenshots:
  - Deep violet/wine purple background (`#1A0B2E`, `#251244`)
  - Vibrant blue tower gradient (`#00B0FF` to `#0055FF`)
  - Glowing orange/gold tower gradient (`#FF9100` to `#FF5722`)
  - Emerald green buttons (`#00E676`)
  - Glowing neon cyan and yellow badges

### Phase 2: Authentic Animated Guild Clash Scoreboard (`GuildClashScoreboardScreen.kt`)
- Build the 1:1 animated split screen from Reference 1:
  - **Top Bar**: Live contest status, season points, and close action.
  - **Left Blue Half**: Animated glowing tower that rises from 0% to score percentage; Team Avatar (e.g. Kohli), Team Name, Avg. Score counter (e.g., 401), Total Score (e.g., 62,983), Member count (150+), and "VIEW" team modal.
  - **Right Golden Half**: Animated glowing tower rising proportionally; Rival Avatar (e.g. Dhoni), Rival Name, Avg. Score counter (e.g., 336), Total Score (e.g., 41,370), Member count (100+), and "VIEW" team modal.
  - **Dynamic Ahead-By Badge**: "AHEAD BY 65" in bright neon green with up-arrow or "BEHIND BY" in red.
  - **Bottom Personal Card**: Player avatar, personal score (`4,336`), contribution percentage, and big glowing "PLAY MORE TO WIN" action button that launches gameplay.
  - **Celebration & Effects**: Smooth number counting animation (`animateIntAsState`), animated tower height progress (`animateFloatAsState`), and victory confetti/pulse.

### Phase 3: Game Lobby & Authentic Tile Grid
- Integrate the full game grid from Reference 7 and 3:
  - `Knife Up`
  - `Fruit Samurai` (renamed from Fruit Slash)
  - `Candy Match`
  - `Carrom Clash`
  - `Space Warrior`
  - `Memory Mania`
  - `Bubble Shooter`
  - `Cricket Mania`
  - `Metro Surfer`
- Add authentic card badges: "Trending", "For You", "Popular", with rankings #1, #2, #3, #4.

### Phase 4: Fantasy Captain & Vice-Captain Selector
- Implement the exact UI from Reference 5:
  - "Choose your Captain and Vice Captain" header
  - `(C) gets 2x points` and `(VC) gets 1.5x points` instruction pills
  - Grouped sections: Wicket-Keeper, Batsmen, All-Rounders, Bowlers
  - Circular player avatars with nationality badges (IND, SA, etc.)
  - Interactive C & VC toggle buttons (highlighting Purple when selected)
  - Bottom sticky bar with "TEAM PREVIEW" and purple "SAVE TEAM" action.

### Phase 5: Transactions History Screen
- Implement the exact UI from Reference 2:
  - Header: Purple background with back arrow and title "TRANSACTIONS"
  - Sub-tabs: "ALL TRANSACTIONS" and "MY PURCHASES"
  - Filter pills: "All", "Tds Transactions", "Add Cash/Withdrawal"
  - Informational box: "You can only view transactions for the last 30 days in this section"
  - Transaction items: Date & time, "Add Cash (incl GST)", "+₹100" in green, and "Help" action button.

### Phase 6: Verification & Compilation
- Execute `compile_applet` to ensure 0 build errors.
- Run local JVM unit tests via `gradle :app:testDebugUnitTest`.
- Update `public/winzo-games.apk` with the latest compiled build.
