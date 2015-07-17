(set-env!
  :resource-paths #{"resources"}
  :dependencies '[[adzerk/bootlaces          "0.1.11" :scope "test"]
                  [cljsjs/boot-cljsjs        "0.5.0"  :scope "test"]
                  [cljsjs/react              "0.13.3-0"]])

(require '[adzerk.bootlaces :refer :all]
  	 '[cljsjs.boot-cljsjs.packaging :refer :all])

(def react-widgets-version "2.6.1")
(def +version+ (str react-widgets-version "-1"))
(bootlaces! (str +version+ "-SNAPSHOT"))

(task-options!
 pom  {:project     'bensu/react-widgets
       :version     (str +version+ "-SNAPSHOT")
       :description  "An a la carte set of polished, extensible, and accessible inputs built for React"
       :url         "https://github.com/jquense/react-widgets"
       :scm         {:url "https://github.com/bensu/packages"}
       :license     {"MIT" "http://opensource.org/licenses/MIT"}})

(deftask download-react-widgets []
  (download :url (str "https://github.com/jquense/react-widgets/archive/v" react-widgets-version ".zip")
            :checksum "224e502fb1a1165e306c59601c4f53e6"
            :unzip    true))

(deftask package []
  (comp
    (download-react-widgets)
    (sift :move {#"^react-widgets-.*/dist/react-widgets.js"
                 "cljsjs/development/react-widgets.inc.js"
                 #"^react-widgets-.*/dist/css/*"
                 "cljsjs/common/"})
    (sift :include #{#"^cljsjs"})
    (deps-cljs :name "cljsjs.react-widgets"
      :no-externs true
      :requires ["cljsjs.react"])))
