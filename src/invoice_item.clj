(ns invoice-item)
(use 'clojure.test)

(defn discount-factor [item]
  (if (and (get item :discount-rate) (> (get item :discount-rate) 0))
    (- 1 (/ (get item :discount-rate) 100.0))
    (- 1 (/ 0 100.0)))
  )

(defn subtotal
  [item]
  (def precise-price (get item :precise-price))
  (def precise-quantity (get item :precise-quantity))
  (float (* precise-price precise-quantity (discount-factor item))))

(deftest datatypes
  (def item1 {:precise-price 5000 :precise-quantity 2 :discount-rate 30})
  (def item2 {:precise-price 5000.0000 :precise-quantity 4/2 :discount-rate 0/99})
  (is (float? (subtotal item1)))
  (is (float? (subtotal item2)))
  )

(deftest output
  (def item1 {:precise-price 5000 :precise-quantity 2 :discount-rate 30})
  (def item2 {:precise-price 5000 :precise-quantity 2 :discount-rate 0})
  (def item3 {:precise-price 5000 :precise-quantity 2})
  (def item4 {:precise-price 1 :precise-quantity 1 :discount-rate 100})
  (def item5 {:precise-price 1000 :precise-quantity 100 :discount-rate 90})
  (is (= (subtotal item1) 7000.0))
  (is (= (subtotal item2) 10000.0))
  (is (= (subtotal item3) 10000.0))
  (is (= (subtotal item4) 0.0))
  (is (= (subtotal item5) 10000.0))
  )

(run-tests)