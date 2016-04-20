(ns cljwallet.core
  (:use compojure.core)
  (:require [clojure.data.json :as json]
            [org.httpkit.client :as http]
            [org.httpkit.server :as server]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [compojure.response :as response]
            [stencil.core :as stencil])
  (:import  (org.bitcoinj.params MainNetParams)
            (org.bitcoinj.core Coin Address AbstractWalletEventListener)
            (org.bitcoinj.kits WalletAppKit)
            (java.io File)))

; Entry point of the program
(defn -main [& args]
  ; Gets the parameters for Main Net
  (def params (. MainNetParams get))
  ; Names the local files that store parts of the block chain
  (def filePrefix "forwarding-service")
  ; Calls the Bitcoinj procedures to get parts of the block chain
  (def kit (new WalletAppKit params (new File ".") filePrefix))
  (. kit startAsync)
  (. kit awaitRunning)
  ; Creates a wallet which holds the Bitcoin addresses and methods
  ; used to manipulate them
  (def wallet (. kit wallet))
  (println "Up and running!")
  (println (. wallet currentReceiveAddress))
  
  ; Creates a listener and overrides its even listener to print
  ; the value of recieved coins
  (def listener (proxy [org.bitcoinj.core.AbstractWalletEventListener] []
                  (onCoinsReveived [w tx prevBal newBal]
                    (println newBal))))
  (. wallet addEventListener listener)


  ; Displays the main webpage
  (defn main-page [req]
    (stencil/render-file "homepage"
                         {:satoshi (. wallet getBalance) :address (. wallet currentReceiveAddress)}))

  ; Handles sending coins
  (defn send-coins [query]
    ; *** Use the map as a function ***
    (def satoshi (query "satoshi"))
    (def address (new Address params (query "address")))
    (def sendReq (. wallet sendCoins (. kit peerGroup) address (. Coin parseCoin satoshi)))
    (str "Amount "satoshi " of Satoshi was sent to address " (. address toString)))

  ; Defines the routes
  (defroutes my-routes
    ; Main page that shows the balance and options available
    (GET "/" [] main-page)
    ; Sends the value 'satoshi' to address and shows the main page
    (GET "/send*" {query :query-params}
                (send-coins query)))

  ; Runs the server at http://localhost:8080/
  (server/run-server (handler/site #'my-routes) {:port 8080}))
