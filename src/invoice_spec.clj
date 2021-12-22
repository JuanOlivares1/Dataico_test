(ns invoice_spec
  (:require [clojure.spec.alpha :as s])
  (:require [clojure.data.json :as json])
  )

(s/def :customer/company_name string?)
(s/def :customer/email string?)
(s/def :invoice/customer (s/keys :req [:customer/company_name
                                       :customer/email]))

(s/def :tax/rate double?)
(s/def :tax/category #{:iva})
(s/def ::tax (s/keys :req [:tax/category
                           :tax/rate]))
(s/def :invoice-item/taxes (s/coll-of ::tax :kind vector? :min-count 1))

(s/def :invoice-item/price double?)
(s/def :invoice-item/quantity double?)
(s/def :invoice-item/sku string?)

(s/def ::invoice-item
  (s/keys :req [:invoice-item/price
                :invoice-item/quantity
                :invoice-item/sku
                :invoice-item/taxes]))

(s/def :invoice/issue-date inst?)
(s/def :invoice/items (s/coll-of ::invoice-item :kind vector? :min-count 1))

(s/def ::invoice
  (s/keys :req [:invoice/issue-date
                :invoice/customer
                :invoice/items]))


(defn my-value-reader [key value]
  (cond
    (= key :tax_rate) (double value)
    (= key :tax_category) (keyword "iva")
    :else value))

(defn prob2 [file] (json/read-str (slurp file)
                                  :value-fn my-value-reader
                                  :key-fn keyword))

(println (s/valid? ::invoice (prob2 "invoice.json")))
(print (prob2 "invoice.json"))
