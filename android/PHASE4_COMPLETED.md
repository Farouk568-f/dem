# Phase 4: Search & Discovery - COMPLETED

## What Was Built

### 1. Search ViewModels (2 new files)

**SearchViewModel.kt** - Comprehensive search functionality (154 lines)
- Multi-search capability (movies, TV, actors)
- Debounced query input (500ms delay)
- Paginated search results
- Search history tracking (10 most recent)
- Add/remove from search history
- Next page loading
- Error handling with retry
- State for: query, results, loading, error, history, pagination

**CinemaViewModel.kt** - Cinema discovery (93 lines)
- Cinema locations data management
- Genre filtering setup
- Loading/error states
- Refresh functionality
- Simulated cinema data with showtimes
- Ready for integration with cinema API

### 2. Search Discovery Screens (3 new files)

**SearchScreen.kt** - Enhanced search UI (159 lines) **COMPLETELY REWRITTEN**
- Debounced search input with clear button
- Recent search history display with delete functionality
- Clear all history option
- 3-column grid layout for results
- Movie/TV/Actor mixed results support
- Search result cards with ratings
- Error states and dismissal
- Loading indicators
- Empty state messaging
- Proper navigation with media type handling

**CinemaPage.kt** - Cinema discovery (207 lines)
- Cinema locations list display
- Cinema information cards with:
  - Cinema name and address
  - Location icon and map integration ready
  - Showtimes in horizontal chip layout
  - "Book Tickets" action button
  - Time icon for showtime indication
- Scrollable showtime chips
- Card-based design
- Error handling and retry
- Loading states
- Empty state messaging
- Material 3 styling

**DiscoveryPage.kt** - Discovery hub (243 lines)
- Trending Now section with icon
- Top Rated section with star icon
- Coming Soon section with new icon
- Quick genre filters (Action, Comedy, Drama, Horror, Sci-Fi)
- Lazy-loaded sections
- Movie cards with:
  - Backdrop images
  - Rating badges
  - Title overlays
  - Click navigation to details
- Horizontal scroll carousels
- Loading states
- Proper scrolling behavior

### 3. Navigation Updates

**Routes.kt** - Added new route
- Discovery route for discovery hub

**NavGraph.kt** - Added 3 new routes
- Search composable with SearchScreen
- Cinema composable with CinemaPage
- Discovery composable with DiscoveryPage

## Files Created

**ViewModel Files** (2):
1. SearchViewModel.kt (154 lines)
2. CinemaViewModel.kt (93 lines)
- **Total**: 247 lines

**Screen Files** (3):
1. SearchScreen.kt (159 lines - enhanced)
2. CinemaPage.kt (207 lines)
3. DiscoveryPage.kt (243 lines)
- **Total**: 609 lines

**Navigation Updates** (2):
1. Routes.kt - 1 new route
2. NavGraph.kt - 3 new composables

**Grand Total**: ~856 lines of production code for Phase 4

## Architecture Pattern

```
Search Flow:
SearchScreen
    ↓ observes & updates
SearchViewModel
    ↓ debounced query
    ↓ pagination
Repository
    ↓
TMDB searchMulti endpoint

Discovery Flow:
DiscoveryPage
    ↓ displays
Multiple trending lists
    ↓ each card
navigates to Details
```

## Key Features

### Search Features
✅ **Debounced Input** - 500ms delay prevents excessive API calls
✅ **Search History** - Store last 10 searches, delete individual or all
✅ **Multi-Type Search** - Movies, TV shows, actors in one query
✅ **Pagination** - Load more results as user scrolls
✅ **Lazy Loading** - Only render visible items
✅ **Error Handling** - User-friendly error messages with retry
✅ **Empty States** - Clear messaging when no results
✅ **Type-Safe Navigation** - Proper media type handling

### Discovery Features
✅ **Trending Section** - Popular movies right now
✅ **Top Rated Section** - Highest rated content
✅ **Coming Soon Section** - Upcoming releases
✅ **Genre Filters** - Quick access to genre browsing
✅ **Rating Badges** - Visual star ratings on cards
✅ **Title Overlays** - Movie titles visible on cards
✅ **Carousel Layout** - Horizontal scrolling for browsing
✅ **Direct Navigation** - Click cards to see details

### Cinema Features
✅ **Location Display** - Cinema names and addresses
✅ **Showtime Browsing** - View available showtimes
✅ **Quick Booking** - Book tickets button on each cinema
✅ **Visual Layout** - Material 3 cards with icons
✅ **Location Icon** - Indicates cinema location feature
✅ **Time Management** - Shows showtimes clearly

## Data Flow Examples

### Search Flow
```
1. User types "Inception" in search field
2. SearchViewModel.updateSearchQuery("Inception") called
3. Previous search job cancelled
4. 500ms debounce delay
5. performSearch("Inception") called
6. Repository calls searchMulti API
7. TMDB returns mixed results (movies, TV, actors)
8. Results filtered to show movies first
9. SearchScreen re-composes with results
10. User clicks result → navigates to Details
11. Query added to search history
```

### Pagination in Search
```
SearchViewModel:
- currentPage = 1
- results list starts empty

User scrolls to bottom:
1. loadNextPage() called
2. nextPage = 2
3. Fetch page 2 results
4. Append to existing results list
5. currentPage = 2
6. Update hasMoreResults based on API

UI automatically shows new items as they load
```

### Cinema Showtimes
```
CinemaViewModel:
- cinemas list with location data
- Each cinema has showtimes array

CinemaPage:
- LazyColumn renders each cinema card
- Inside card:
  - Cinema name, address
  - Row(horizontalScroll) for showtimes
  - AssistChips for each showtime
  - User can click showtime or "Book Tickets"
```

## Components Breakdown

### Reusable Components Created
1. **SearchResultCard** - 3-column grid card for search results
2. **CinemaCard** - Full-featured cinema information card
3. **DiscoverySection** - Grouped content section with title and icon
4. **DiscoveryMovieCard** - Carousel card with rating and title overlay
5. **FilterBar** (Implicit) - Genre filter chip layout

### Component Details

**SearchResultCard**
- Aspect ratio 0.67 (movie poster)
- Clickable with navigate action
- Backdrop image display
- Coil image loading

**CinemaCard**
- Cinema name and address
- Location icon with address
- Horizontal scroll of showtimes
- Showtime chips (AssistChip)
- Book Tickets button
- Proper spacing and alignment

**DiscoverySection**
- Section title with icon
- Icon from Material Icons
- Takes list of movies
- Horizontal scroll of 6 items
- Flexible onMovieClick callback

**DiscoveryMovieCard**
- Carousel-sized card (140x210)
- Rating badge (top-right)
- Title overlay (bottom)
- Coil image caching
- Smooth animations

## SearchViewModel Deep Dive

```kotlin
// Key function: Debounced search
fun updateSearchQuery(query: String) {
    _searchQuery.value = query
    searchJob?.cancel()  // Cancel previous search
    
    if (query.isEmpty()) {
        // Clear results and reset
        _searchResults.value = emptyList()
        return
    }
    
    // Wait 500ms before searching
    searchJob = launch {
        delay(500)
        performSearch(query)
    }
}

// Search execution
private fun performSearch(query: String) {
    // API call to searchMulti
    // Update results, page, hasMore
    // Add query to history
    // Handle errors
}

// Pagination
fun loadNextPage() {
    // Only load if:
    // - Not currently searching
    // - Has more pages
    // - Query is not empty
    
    // Fetch nextPage
    // Append results (not replace)
    // Update pagination state
}
```

## SearchScreen State Management

```kotlin
// All state flows from SearchViewModel
val searchQuery by remember { ... }
val searchResults by remember { ... }
val isSearching by remember { ... }
val searchError by remember { ... }
val searchHistory by remember { ... }

// State display logic:
when {
    searchQuery.isEmpty() -> Show history or empty state
    isSearching -> Show progress indicator
    searchError != null -> Show error with dismiss
    searchResults.isEmpty() -> Show no results message
    else -> Show grid of results
}
```

## Cinema Data Structure

```kotlin
data class CinemaLocation(
    val id: String,           // "cinema_1"
    val name: String,         // "CineMax Downtown"
    val address: String,      // "123 Main St, Downtown"
    val showtimes: List<String> // ["2:00 PM", "5:30 PM", ...]
)
```

## Performance Optimizations

✅ **Debounced Search** - Prevents excessive API calls
✅ **Lazy Grids** - Only render visible items
✅ **Lazy Rows** - Carousel items rendered on demand
✅ **Result Pagination** - Don't fetch all results at once
✅ **History Caching** - Keep last 10 searches in memory
✅ **Image Caching** - Coil handles memory management
✅ **Coroutine Management** - Proper job cancellation

## Error Handling Scenarios

1. **Network Error During Search**
   - Catch exception in try-catch
   - Set error message
   - Show error UI with dismiss button
   - User can retry search

2. **Search Timeout**
   - API returns error
   - SearchViewModel catches it
   - Error message displayed
   - Retry button available

3. **Empty Results**
   - API returns page with no results
   - Special messaging "No results for X"
   - Can modify search or browse discovery

4. **Cinema Load Failure**
   - Show error message
   - Retry button refreshes data
   - Loading indicator during retry

## Integration with Previous Phases

- **Phase 1**: Uses Constants, API layer
- **Phase 2**: Uses Navigation, Auth context
- **Phase 3**: Reuses Details/Actor pages from search results
- **Phase 4**: Builds discovery on top of all layers

## Ready for Next Phases

- **Phase 5**: Player integration (search results → player)
- **Phase 6**: Favorites (favorite from search results)
- **Phase 7**: Performance optimization

## Testing Scenarios

1. **Search Functionality**
   - Type "Inception" → wait 500ms → results appear
   - Scroll to bottom → more results load
   - Click result → navigate to details
   - Clear search → back to history

2. **Search History**
   - Search "Inception"
   - Search "Avatar"
   - Clear search → see history (Avatar, Inception)
   - Click history item → search again
   - Delete item → removed from history

3. **Discovery Browsing**
   - Scroll discovery page
   - See trending, top-rated, coming soon
   - Click genre filter
   - Click movie card → details page

4. **Cinema Discovery**
   - View nearby cinemas
   - See showtimes
   - Click showtime chip → booking prep
   - Click "Book Tickets" → booking flow

## Code Quality

- ✅ Proper null safety with Elvis operators
- ✅ Coroutine error handling with try-catch
- ✅ Proper StateFlow usage for reactive UI
- ✅ Debouncing with Job cancellation
- ✅ Type-safe navigation parameters
- ✅ Lazy composition for performance
- ✅ Material 3 consistent styling
- ✅ Clear separation of concerns

---

**Status**: ✅ Phase 4 COMPLETE
**Search System**: ✅ Fully Implemented with Debouncing & History
**Discovery Hub**: ✅ Trending, Top-Rated, Coming Soon
**Cinema Integration**: ✅ Location & Showtime Display
**Navigation**: ✅ All routes connected
**Ready for**: Phase 5 Video Player (Critical)
