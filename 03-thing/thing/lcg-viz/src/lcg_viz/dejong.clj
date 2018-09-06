(ns ws-ldn-10.ex01
  (:require
   [thi.ng.math.core :as m]
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

(defn compute-dejong
  "Computes a single DeJong 2d point vector for given params and XY pos"
  [a b c d x y]
  (v/vec2
   (+ (Math/sin (* a y)) (Math/cos (* (* b x) x)))
   (+ (Math/sin (* (* c x) x)) (Math/cos (* d y)))))

(defn dejong-svg
  [{:keys [width iter a b c d color bg]}]
  (let [scale  (/ width 4)
        center (v/vec2 (/ width 2))]
    (->> (range iter)
         ;; iterative system: f(x+1) = f(f(x))
         (reduce
          (fn [[points [x y]] _]
            (let [pos (compute-dejong a b c d x y)]
              [(conj points (svg/circle (m/madd pos scale center) 1)) pos]))
          ['() [(m/random width) (m/random width)]])
         (first)
         (svg/group
          {:fill (or color "rgba(0,0,0,0.25)") :stroke "none"}
          (if bg (svg/rect [0 0] width width {:fill bg})))
         (svg-doc width))))

  (spit "dejong.svg"
        (dejong-svg
         {:width 600
          :iter  10000
          :a     (m/random -3 3)
          :b     (m/random -3 3)
          :c     (m/random -3 3)
          :d     (m/random -3 3)}))

  