(ns sketches.sketch-perlin-flow
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [lib.key-press-handlers :refer [on-key-press]]
            [sketches.palette :refer [find-palette]]))

;; A2
(def w 2480)
(def h 3508)


(def palette (find-palette "ducci_h"))
(def noise-dim 350)
(def diameter 10)
(def angle 3)

(defn particle []
  {:x     (* w (rand))
   :y     (* h (rand))
   :vx    0
   :vy    0
   :color-index (rand-int (count (:colors palette)))
   :adir  0})


(defn particles [n]
  (map (fn [] (particle)) (range n)))


(defn update-pos [curr delta max]
  (mod (+ curr delta) max))


(defn update-vel [curr delta]
  (q/norm (+ curr delta) 0 2))


(defn update-acc [curr x y]
  (q/map-range (q/noise (/ x noise-dim) (/ y noise-dim))
               0
               1
               (* angle (- Math/PI))
               (* angle Math/PI)))


(defn sketch-update [state]
  (map (fn [p]
         (assoc p
                :x  (update-pos (:x p) (:vx p) w)
                :y  (update-pos (:y p) (:vy p) h)
                :vx (update-vel (:vx p) (Math/cos (:adir p)))
                :vy (update-vel (:vx p) (Math/sin (:adir p)))
                :adir (update-acc (:adir p) (:x p) (:y p))))
       state))

(defn sketch-draw [state]
  (doseq [pnt state]
    (apply q/fill (nth (:colors palette) (:color-index pnt)))
    (q/ellipse (:x pnt) (:y pnt) diameter diameter)))

(defn save-image [state]
  (when (= "s" (q/raw-key))
    (q/save (str (js/prompt "Enter name of the sketch to save:") ".jpeg")))
  state)


(defn create [canvas]
  (q/defsketch perlin-flow
    :host canvas
    :size [w h]
    :setup (fn []
             (q/no-stroke)
             (apply q/background (:background palette))
             (particles 1000))
    :update sketch-update
    :draw sketch-draw
    :middleware [m/fun-mode]
    :key-pressed save-image))
