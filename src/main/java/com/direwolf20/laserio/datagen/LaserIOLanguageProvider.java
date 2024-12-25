package com.direwolf20.laserio.datagen;

import com.direwolf20.laserio.common.LaserIO;
import com.direwolf20.laserio.setup.Registration;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

import static com.direwolf20.laserio.setup.ModSetup.TAB_NAME;

public class LaserIOLanguageProvider extends LanguageProvider {
    public LaserIOLanguageProvider(PackOutput output, String locale) {
        super(output, LaserIO.MODID, locale);
    }

    @Override
    protected void addTranslations() {



        //add("", "");
    }
}