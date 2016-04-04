(ns cljwallet.core
  (:use compojure.core)
  (:require [clojure.data.json :as json]
            [org.httpkit.client :as http]
            [org.httpkit.server :as server]
            [compojure.route :as route]
            [compojure.handler :as handler]
            [compojure.response :as response])
  (:import  (org.bitcoinj.params MainNetParams)
            (org.bitcoinj.core Address AbstractWalletEventListener)
            (org.bitcoinj.kits WalletAppKit)
            (java.io File)))

;(defn -main [& args]
;  (def addressInfo @(http/get "https://api.blockcypher.com/v1/btc/main/addrs/1BvBMSEYstWetqTFn5Au4m4GFg7xJaNVN2"))
;  (let [{a :body} addressInfo]
;    (print a)))

(defn -main [& args]
  (def params (. MainNetParams get))
  (def filePrefix "forwarding-service")
;  (def forwardingAddress (new Address params "1BvBMSEYstWetqTFn5Au4m4GFg7xJaNVN2"))
  (def kit (new WalletAppKit params (new File ".") filePrefix))
  (. kit startAsync)
  (. kit awaitRunning)
  (def wallet (. kit wallet))
  (println (. (. wallet getBalance) getValue))
  
  (def listener (proxy [org.bitcoinj.core.AbstractWalletEventListener] []
                  (onCoinsReveived [w tx prevBal newBal]
                    (println newBal))))
  (. wallet addEventListener listener)



  (defn show-landing-page [req] (str "Landing Page."))
  (defn show-send-page [req] (str "Send Page."))

  ;; USE COMPOJURE INSTEAD!!!
  (defroutes my-routes
    (GET "/" [] show-landing-page)
    (GET "/bar" [] "Hello Bar")
    (POST "/spend" req
          (let [title (get (:params req) :title)]
            (println "Title: " title))))
;  (defn app [req]
;    {:status 200
;     :header {"Content-Type" "text/html"}
;     :body (str "Balance: " (. (. wallet getBalance) getValue))})
  (server/run-server (handler/site #'my-routes) {:port 8080}))
