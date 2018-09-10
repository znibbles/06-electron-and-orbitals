(ns lcg_viz
  (:require
   [thi.ng.math.core :as m]
   [thi.ng.math.macros :as mm]
   [thi.ng.geom.core :as g]
   [thi.ng.geom.matrix :as mat]
   [thi.ng.geom.vector :as v]
   [thi.ng.geom.sphere :as s]
   [thi.ng.geom.svg.core :as svg]
   [thi.ng.geom.svg.adapter :as svgadapt]
   [thi.ng.geom.svg.shaders :as shader]
   [thi.ng.geom.svg.renderer :as render]
   [thi.ng.color.core :as col]))


(def width    600)
(def height   600)
(def model    (g/rotate-y (mat/matrix44) m/SIXTH_PI))
(def view     (apply mat/look-at (mat/look-at-vectors 1.3 1 1.5 0 0 0)))
(def proj     (mat/perspective 40 (/ width height) 0.1 10))
(def mvp      (->> model (m/* view) (m/* proj)))

(def uniforms {:stroke "none" :stroke-width 0})

(def shader-diffuse
  (shader/shader
   {:fill     [0.1 0.1 0.3]
    :uniforms uniforms
    :flags    {:solid true}}))

(def shader-phong
  (shader/shader
   {:fill     (shader/phong
               {:model     model
                :view      view
                :light-pos [-1 2 1]
                :light-col [1 1 1]
                :diffuse   [0.2 0.5 0.75]
                :ambient   [0.05 0.05 0.2]
                :specular  0.8
                :shininess 8.0})
    :uniforms uniforms
    :flags    {:solid true}}))    

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
      acc
      (recur (conj acc (lcg (last acc) a c m))))))

(defn lcg-svg-3d
  [{:keys [width start a c m iter color bg]}]
  (->> (lcg-seq start a c m iter)
        (partition 3)
        (map
          (fn [point]
            (let [radius (m/random 0.003 0.007)]
            (->
              (->> (vec point)
                (map #(- (/ % m) 0.5))
                (v/vec3))
              (s/sphere radius)
              (g/as-mesh)
              (render/mesh mvp (mat/viewport-matrix width width) shader-phong)
              ))))
        (svg/group
          {:fill "none" :stroke "none"}
          (if bg (svg/rect [0 0] width width {:fill bg}))
          )
        (svg-doc width))) 

; RANDU
(spit "lcg-viz-3d.svg" (lcg-svg-3d {
  :width 600
  :start 1
  :a 65539
  :c 0
  :m (.pow (BigInteger. "2") 31)
  :iter 6993
  :bg "rgba(0,0,10,1)"}))
