(ns lcg_viz
  (:require
   [thi.ng.math.core :as m]
   [thi.ng.math.macros :as mm]
   [thi.ng.geom.core :as g]
   [thi.ng.geom.vector :as v]
   [thi.ng.geom.circle :as c]
   [thi.ng.geom.rect :as r]
   [thi.ng.geom.polygon :as poly]
   [thi.ng.geom.svg.core :as svg]
   [thi.ng.geom.svg.adapter :as svgadapt]
   [thi.ng.color.core :as col]))

(defn svg-doc
  [width body]
  (->> body
       (svg/svg {:width width :height width})
       (svgadapt/all-as-svg)
       (svg/serialize)))

(defn lcg
  "Computes the next LCG value for given increment, multiplier and modulus"
  [x_n a c m]
  (-> x_n
      (* a)
      (+ c)
      (mod m)))

(defn lcg-seq
  "Computes a sequence of LCG values"
  [start a c m iter]
  (loop [acc [start]]
    (if (>= (count acc) iter)
      (subvec acc 1 iter)
      (recur (conj acc (lcg (last acc) a c m))))))

(defn lcg-svg-2d
  [{:keys [width start a c m iter color bg]}]
  (->> (lcg-seq start a c m iter)
        (partition 2)
        (map
          (fn [point]
            (let [red (m/random 0.3 0.6) 
                  green (m/random 0.7 0.9) 
                  blue (m/random 0.7 1)
                  radius (m/random 1 3)]
            (->
              (->> (vec point)
                (map #(* (/ % m) width))
                (v/vec2))
              (svg/circle radius {:fill (col/rgba red green blue 0.6)})))))
        (svg/group
          {:fill "none" :stroke "none"}
          (if bg (svg/rect [0 0] width width {:fill bg}))
          )
        (svg-doc width)))


(spit "lcg-viz-2d.svg" (lcg-svg-2d {
  :width 600
  :start 0 
  :a 29 
  :c 177 
  :m 82847 
  :iter 20001
  :bg "rgba(0,0,20,1)"}))
