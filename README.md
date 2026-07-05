# Rock - discord bot for records keeping

#### (README Language/ Язык README)
[![English](https://img.shields.io/badge/Language-English-blue)](README.md)
[![Русский](https://img.shields.io/badge/Language-Русский-green)](README.ru-RU.md)

![rock](./rock.jpg)
# What is he?
Originally started as a simple "read JSON" bot but has been growing into so much more

# How to Install

## Guide

*Work In Progress*

Things change rapidly, ill update README at some point

# Structure

## discord_bot
The bot itself. Does bot things
### Commands:
#### /achievements
- link to list of achievements to 1KU composed by iKouRyuu.
#### /check
- Pulls map data from PostgreSQL Database generated and managed by **google_api_handler**
#### /coinflip
- Flips a coin with 3 optional fields of ***reason***, what are ***heads***, what are ***tails***
#### /gamba
- unnecessarily complicated European Roulette made for fun. No debt mechanic, dw.
#### /help
- this command.
#### /misc_stuff
- command for random things. Check it's description to see the current ongoing gig.
#### /random_loadout
- gives you a randomized TF2 loadout to play with.
#### /teach
- Adds a phrase to Dictionary at`/discord_bot/resources/dictionary.yml`
#### /update_any
- update current WR for a map in Any% category. Needs **imgbb.com** links to screenshots of victory screens of all stages for verification.
#### /update_solo
- update current WR for a map in Any% category. Should have both **imgbb.com** links to screenshots and YT link to recording of the map.

  
### Other features
- upon being @'d in dedicated **#the-cave** channel pulls a random phrase out of the **dictionary** filled by **/teach**.
  - if @ message contains "?" returns random Magic 8-Ball answer
  - if @ message follows structure "@rock [...] number between *number* and *number*" returns a random number within the boundaries (including them).
  - upon being @'d in **currently-gaming** channel with structure "@rock [...] mute @user [...]" times them out for 30 seconds. No you can't do it to admis

## google_api_handler
Updates `/shared_resources/records.json` and PSQL Database with data from **Google Sheets** once every hour

## dashboard
Currently holds API to access records. In future will have visual representation, monitoring and tuning web panels.

## PostgreSQL
Primary source for records data