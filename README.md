# Rexplore

**App name:** Rexplore  
**Authors:** Katie Lee & Jesse Masciarelli  
**Platform:** Android  
**Note:** Please see below for a demo of our app!

## Overview: 
Rexplore is an application where users can go for quick music, TV show, and movie suggestions. Users can prompt the app with media that they know they enjoy, and will receive the best music, show, or movie matches back.  
### App purpose:
The purpose of the app is very straightforward, to take in a piece of media (music/movie/show) or genre (rap music/horror movie) that the user enjoys, and give back a list of recommendations that fit their taste. It will be the perfect, instant feedback tool for someone looking to find a new song, or quickly jump into an enjoyable new movie. Making use of the TasteDive API, the app will reap all of the benefit of TasteDive’s large database of media information, but it will be processed and presented in the simplest, most digestible way to the end user to optimize their experience.

### Features:

:star: **Multiple Category Support:** Switch between "music", "movie", and "show" tabs to explore various forms of entertainment

:star: **Simple Recommendation Engine:** Upon searching, instantly receive multiple recommendations based on your search query powered by the TasteDive API

:star: **Interact With Results:** For each recommendation you receive, click on it to see more information and be presented with further options

:star: **Watch Youtube Previews:** Recommendations that you select for all forms of media are oftentimes presented with relevant videos (if applicable)

:star: **Cascade Your Search:** With any selected recommendation, have the option to "See similar" to perform a new query and find even more related content

:star: **Save For Later List:** Save recommendations to three different types of "Save for later" lists to keep track of what you want to check out for a particular type of media, and have access to these list across all executions of the application


### Target audience:
Our app is targeted at those who live and thrive in the era of fast-paced, on-demand services. The aim is to allow users to quickly find a handful of media recommendations in just a few clicks. Little will be offered in the form of “extra information”, so the app will be the perfect tool for those users of any age looking to get right into  something new.

## APIs:
### TasteDive
[API|TasteDive](https://tastedive.com/read/api) - TasteDive offers a recommendation engine capable of returning JSON or JSONP formatted data associated with music, movies, TV shows, books, authors, games, and podcasts.
### API Usage
The TasteDive API will be used as the recommendation engine, with options to query by media type (movie, music, etc), or by media title. With each query, we can first use the basic response (detailed in the TasteDive Documentation below) to present a user with recommendations.  

Variations on this standard usage can include the ability to present the user with quick links to Wikipedia articles and YouTube clips within the app, also provided by a modified call to the TasteDive API.  

We plan to manipulate the API’s recommendation responses to pull a handful of random suggestions. This will give users another way to start exploring, introduce new artists/movies/shows, and maybe spice up their taste.  

Documentation: [TasteDive API Documentation](https://tastedive-api-documentation.readthedocs.io/en/latest/index.html)

## Design:
### Final UI Designs:
From left to right, top to bottom:
- Screen 1: Explore Artists main page - user can search for an artist they're familiar with
- Screen 2: User searches for an artist and can see recommendations based of their search
- Screen 3: User clicks on a recommendation and sees a popup containing more information of that recommendation
- Screen 4: User saves the recommendation to the "Save for later" list
- Screen 5: The user can see their "Save for later" list for the Musical Artist type
- Screen 6: The user can remove any item from their "Save for later" list

<img width="800" alt="screens123" src="https://user-images.githubusercontent.com/31972297/145903490-9f6a1853-db4d-443a-a57f-40f3ac103af5.png">
<img width="800" alt="screens456" src="https://user-images.githubusercontent.com/31972297/145903493-2ef98350-4aa9-45bf-aba0-a24ac07cfbf4.png">

### Rexplore DEMO:
<img width="300" alt="demo_part1" src="https://user-images.githubusercontent.com/31972297/145904647-da3eb550-8545-4b18-968a-b0f8f61b0c34.gif">
<img width="300" alt="demo_part2" src="https://user-images.githubusercontent.com/31972297/145904653-8cff61fa-dac9-4450-8a9b-ae3ce803e2c5.gif">

### UI Mockups:
From left to right:
- Screen1_ExploreMenu
- Screen2_ExploreRandoms
- Screen2.1_Expanded
- Screen3_ExploreMMS
  
<img width="799" alt="display4" src="https://user-images.githubusercontent.com/31972297/143140092-8c9f1911-0735-4b86-86fb-fec528a84438.png">

### Control Flow:
<img width="686" alt="control_flow_diagram" src="https://user-images.githubusercontent.com/31972297/143141488-29af5457-36d7-4cca-9c4a-8e132c262c19.png">
