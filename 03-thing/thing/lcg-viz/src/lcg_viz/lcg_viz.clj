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

(defn lcg-svg
  [{:keys [width start a c m iter color bg]}]
  (->> (lcg-seq start a c m iter)
        (partition 2)
        (map
          (fn [point]
            (->
              (->> (vec point)
                (map #(* (/ % m) width))
                (v/vec2))
              (c/circle 1))))
        (svg/group
          {:fill (or color "rgba(0,0,0,0.25)") :stroke "none"}
          (if bg (svg/rect [0 0] width width {:fill bg}))
          )
        (svg-doc width)))


(spit "lcg-viz.svg" (lcg-svg {
  :width 600
  :start 0 
  :a 29 
  :c 177 
  :m 82847 
  :iter 10001
  :color "rgba(255,220,200,0.5)"
  :bg "rgba(0,0,20,1)"}))
