package com.fitness.application.gym.exercises.entity;

import java.util.Locale;

import org.springframework.context.MessageSource;

import lombok.Getter;

@Getter
public enum Muscle {
    ANTERIOR_DELTOID("muscle.anterior_deltoid", Category.SHOULDERS),
    LATERAL_DELTOID("muscle.lateral_deltoid", Category.SHOULDERS),
    POSTERIOR_DELTOID("muscle.posterior_deltoid", Category.SHOULDERS),

    PECTORALIS_MAJOR_CLAVICULAR("muscle.pectoralis_major_clavicular", Category.CHEST),
    PECTORALIS_MAJOR_STERNOCOSTAL("muscle.pectoralis_major_sternocostal", Category.CHEST),

    LATISSIMUS_DORSI("muscle.latissimus_dorsi", Category.BACK),
    TRAPEZIUS("muscle.trapezius", Category.BACK),
    RHOMBOIDS("muscle.rhomboids", Category.BACK),
    TERES_MAJOR("muscle.teres_major", Category.BACK),
    ERECTOR_SPINAE("muscle.erector_spinae", Category.BACK),

    BICEPS_BRACHII("muscle.biceps_brachii", Category.ARMS),
    BRACHIALIS("muscle.brachialis", Category.ARMS),
    TRICEPS_BRACHII("muscle.triceps_brachii", Category.ARMS),
    BRACHIORADIALIS("muscle.brachioradialis", Category.ARMS),
    FOREARM_FLEXORS("muscle.forearm_flexors", Category.ARMS),
    FOREARM_EXTENSORS("muscle.forearm_extensors", Category.ARMS),

    QUADRICEPS("muscle.quadriceps", Category.LEGS),
    HAMSTRINGS("muscle.hamstrings", Category.LEGS),
    ADDUCTORS("muscle.adductors", Category.LEGS),

    GLUTEUS_MAXIMUS("muscle.gluteus_maximus", Category.GLUTES),
    GLUTEUS_MEDIUS("muscle.gluteus_medius", Category.GLUTES),
    GLUTEUS_MINIMUS("muscle.gluteus_minimus", Category.GLUTES),

    GASTROCNEMIUS("muscle.gastrocnemius", Category.CALVES),
    SOLEUS("muscle.soleus", Category.CALVES),

    RECTUS_ABDOMINIS("muscle.rectus_abdominis", Category.CORE),
    EXTERNAL_OBLIQUE("muscle.external_oblique", Category.CORE),
    INTERNAL_OBLIQUE("muscle.internal_oblique", Category.CORE),
    TRANSVERSUS_ABDOMINIS("muscle.transversus_abdominis", Category.CORE);

    private final String code;
    private final Category category;

    Muscle(String code, Category category) {
        this.code = code;
        this.category = category;
    }

    @Getter
    public enum Category {
        SHOULDERS("category.shoulders"),
        CHEST("category.chest"),
        BACK("category.back"),
        ARMS("category.arms"),
        LEGS("category.legs"),
        GLUTES("category.glutes"),
        CALVES("category.calves"),
        CORE("category.core");

        private final String code;

        Category(String code) {
            this.code = code;
        }

        public String getLocalizedName(MessageSource messageSource, Locale locale) {
            return messageSource.getMessage(this.code, null, locale);
        }
    }

    public String getLocalizedName(MessageSource messageSource, Locale locale) {
        return messageSource.getMessage(this.code, null, locale);
    }
}