package com.llamalad7.mixintests.tests.mixins.plugins;

import com.llamalad7.mixintests.ap.annotations.Config;
import com.llamalad7.mixintests.ap.annotations.MixinTest;
import com.llamalad7.mixintests.harness.tests.TestBox;
import com.llamalad7.mixintests.tests.targets.EmptyTarget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

@MixinTest(box = EmptyTarget.class)
public class LegacyConfigPlugin {
    @Config(plugin = ThePlugin.class)
    @Mixin(EmptyTarget.class)
    static class TheMixin {
    }

    public static class ThePlugin implements IMixinConfigPlugin {
        @Override
        public void onLoad(String mixinPackage) {
        }

        @Override
        public String getRefMapperConfig() {
            return null;
        }

        @Override
        public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
            return true;
        }

        @Override
        public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
        }

        @Override
        public List<String> getMixins() {
            return null;
        }

        // @Override
        public void preApply(String targetClassName, org.spongepowered.asm.lib.tree.ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
            TestBox.print("preApply: " + targetClass.name);
        }

        // @Override
        public void postApply(String targetClassName, org.spongepowered.asm.lib.tree.ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
            TestBox.print("postApply: " + targetClass.name);
        }
    }
}
