package com.example.androidapp1.utils;

import com.example.androidapp1.models.Cone;

public class AddCones {/*
    Cone cone2 = new Cone();
        cone2.setName("Echoes of the Forgotten");
        cone2.setDescription("In the depths of silence, the echoes of past words reverberate, telling a story known to none. Whispering tales of courage and sacrifice, love and loss, they serve as an invisible monument to the lives once lived. Listen closely and you might hear their melodies.");
        cone2.setAbility("Resonance of Valor: Increases ATK by 10%");
        cone2.setBase_hp(22);
        cone2.setBase_atk(18);
        cone2.setBase_def(20);
        cone2.setHp_growth(12);
        cone2.setAtk_growth(10);
        cone2.setDef_growth(14);
        cone2.setRarity(3);
        cone2.setImageResource(getDrawableCone(2));

    Cone cone3 = new Cone();
        cone3.setName("Veil of Serenity");
        cone3.setDescription("An enigma swathed in tranquility, a sanctuary amid the storms of life. It carries the promise of solace, a balm for the weary and the lost. Beneath its tranquil surface, secrets slumber, hidden from the prying eyes of the world, waiting for the chosen one to awaken them.");
        cone3.setAbility("Serenity's Shield: Grants 40% DEF when HP is below 50%");
        cone3.setBase_hp(22);
        cone3.setBase_atk(16);
        cone3.setBase_def(22);
        cone3.setHp_growth(12);
        cone3.setAtk_growth(10);
        cone3.setDef_growth(16);
        cone3.setRarity(3);
        cone3.setImageResource(getDrawableCone(3));

    Cone cone4 = new Cone();
        cone4.setName("Glimmer of Hope");
        cone4.setDescription("Nestled within the boundless dark, a fleeting spark flickers. A symbol of hope against the tides of despair. Those who hold it might sense an elusive warmth, a comforting light guiding them through life's labyrinthine paths, nurturing their faltering resolve.");
        cone4.setAbility("Hope's Resilience: Grants invulnerability for 2 rounds after a fatal blow is dealt to the character");
        cone4.setBase_hp(22);
        cone4.setBase_atk(20);
        cone4.setBase_def(18);
        cone4.setHp_growth(12);
        cone4.setAtk_growth(14);
        cone4.setDef_growth(12);
        cone4.setRarity(3);
        cone4.setImageResource(getDrawableCone(4));

    Cone cone5 = new Cone();
        cone5.setName("Whispers of Time");
        cone5.setDescription("Chronology's murmurs captured in an ageless relic. The tick-tocking of its inner mechanism a subtle but ever-present reminder of life's fleeting moments. Hold it close, and you may glimpse a fragmentary vision of yesterdays and tomorrows, bound by the irrefutable law of causality.");
        cone5.setAbility("Temporal Rejuvenation: Heals 5% each round");
        cone5.setBase_hp(22);
        cone5.setBase_atk(16);
        cone5.setBase_def(20);
        cone5.setHp_growth(12);
        cone5.setAtk_growth(12);
        cone5.setDef_growth(14);
        cone5.setRarity(3);
        cone5.setImageResource(getDrawableCone(5));

    Cone cone6 = new Cone();
        cone6.setName("Mists of Solitude");
        cone6.setDescription("A cloud of seclusion, carried by those who seek to detach themselves from the cacophony of existence. It veils its keeper in an ephemeral mist, where silence sings its lullaby, and solitude becomes a treasured companion.");
        cone6.setAbility("Solitary Strength: Increases ATK by 15%");
        cone6.setBase_hp(22);
        cone6.setBase_atk(20);
        cone6.setBase_def(18);
        cone6.setHp_growth(12);
        cone6.setAtk_growth(14);
        cone6.setDef_growth(12);
        cone6.setRarity(3);
        cone6.setImageResource(getDrawableCone(6));

    Cone cone7 = new Cone();
        cone7.setName("Eclipsed Moon");
        cone7.setDescription("Emblematic of the transient nature of light and darkness, this object bears the visage of a moon concealed by shadow. A tactile metaphor for the ebb and flow of fortune, symbolizing the quiet strength needed to endure life's cyclical challenges.");
        cone7.setAbility("Lunar Resonance: Increases Crit DMG by 20%");
        cone7.setBase_hp(22);
        cone7.setBase_atk(18);
        cone7.setBase_def(20);
        cone7.setHp_growth(12);
        cone7.setAtk_growth(14);
        cone7.setDef_growth(14);
        cone7.setRarity(3);
        cone7.setImageResource(getDrawableCone(7));

    Cone cone8 = new Cone();
        cone8.setName("Shattered Illusions");
        cone8.setDescription("A mirror cracked, yet strangely whole—each shard reflecting a distorted reality. To those who peer into it, the fractures serve as a reminder of the mutable nature of perception, urging a mindful dance between skepticism and faith.");
        cone8.setAbility("Illusionary Fortitude: After using ultimate, your ATK and DEF increase by 10% (60% max)");
        cone8.setBase_hp(22);
        cone8.setBase_atk(22);
        cone8.setBase_def(16);
        cone8.setHp_growth(12);
        cone8.setAtk_growth(12);
        cone8.setDef_growth(12);
        cone8.setRarity(3);
        cone8.setImageResource(getDrawableCone(8));

    Cone cone9 = new Cone();
        cone9.setName("Codex of the Unfathomable");
        cone9.setDescription("A collection of esoteric knowledge, so arcane that its pages appear to be woven from the fabric of existence itself. In its silent script, it hints at the ineffable truths that lie just beyond the boundary of human comprehension, accessible only to those with an unwavering commitment to understanding.");
        cone9.setAbility("Arcane Revelation: Increases Crit Rate and Crit DMG by 40%");
        cone9.setBase_hp(28);
        cone9.setBase_atk(24);
        cone9.setBase_def(20);
        cone9.setHp_growth(14);
        cone9.setAtk_growth(12);
        cone9.setDef_growth(10);
        cone9.setRarity(4);
        cone9.setImageResource(getDrawableCone(9));

    Cone cone10 = new Cone();
        cone10.setName("Resonance of the Void");
        cone10.setDescription("Capturing the essence of emptiness, this object hums with the inaudible frequencies of the cosmos. Its vibrations pierce the veils of reality, allowing brief communion with the unfathomable depths of the multiverse, where the lines between creation and annihilation blur into one.");
        cone10.setAbility("Void Resonance: Gains 20% ATK and 20% DEF when facing cosmic entities");
        cone10.setBase_hp(28);
        cone10.setBase_atk(22);
        cone10.setBase_def(24);
        cone10.setHp_growth(14);
        cone10.setAtk_growth(12);
        cone10.setDef_growth(14);
        cone10.setRarity(4);
        cone10.setImageResource(getDrawableCone(10));

    Cone cone11 = new Cone();
        cone11.setName("Vortex of Forgotten Dreams");
        cone11.setDescription("Swirling within this enigmatic artifact are the nebulous traces of dreams forsaken, ambitions abandoned, and desires unmet. Yet it serves not as a memento of regret, but as a catalyst for change, invigorating its possessor with the vigor to reclaim their unfulfilled aspirations.");
        cone11.setAbility("Dream Reclamation: Regenerates 10% HP at the start of each turn and gains 20% ATK");
        cone11.setBase_hp(26);
        cone11.setBase_atk(20);
        cone11.setBase_def(20);
        cone11.setHp_growth(12);
        cone11.setAtk_growth(12);
        cone11.setDef_growth(12);
        cone11.setRarity(4);
        cone11.setImageResource(getDrawableCone(11));

    Cone cone12 = new Cone();
        cone12.setName("Nexus of Cosmic Synchronicity");
        cone12.setDescription("An intricate web of ethereal connections, where the lines of fate intersect with the pathways of chance. This object is the embodiment of serendipity, allowing its wielder a glimpse of the underlying harmony that orchestrates the chaos of existence.");
        cone12.setAbility("Synchronicity Nexus: 50% chance to dodge incoming attacks and gains 30% Crit Rate");
        cone12.setBase_hp(24);
        cone12.setBase_atk(22);
        cone12.setBase_def(18);
        cone12.setHp_growth(14);
        cone12.setAtk_growth(14);
        cone12.setDef_growth(10);
        cone12.setRarity(4);
        cone12.setImageResource(getDrawableCone(12));

    Cone cone13 = new Cone();
        cone13.setName("Tempest of Calamity");
        cone13.setDescription("Encased within is a storm so fierce it could tear asunder the very fabric of reality. Yet paradoxically, it also holds the potential for rebirth and rejuvenation, granting its master the power to wield devastation as a tool for profound transformation.");
        cone13.setAbility("Calamity's Fury: Increases ATK by 30% and has a 20% chance to unleash a devastating AOE attack");
        cone13.setBase_hp(26);
        cone13.setBase_atk(26);
        cone13.setBase_def(16);
        cone13.setHp_growth(12);
        cone13.setAtk_growth(16);
        cone13.setDef_growth(8);
        cone13.setRarity(4);
        cone13.setImageResource(getDrawableCone(13));

    Cone cone14 = new Cone();
        cone14.setName("Phantasmal Crucible");
        cone14.setDescription("Within this ethereal vessel, the realm of imagination merges with palpable reality. Shadows and echoes from parallel dimensions dance to an unheard rhythm, inviting its possessor to partake in the sublime tapestry of the metaphysical.");
        cone14.setAbility("Dimensional Fusion: 40% chance to replicate the abilities of an opponent upon receiving damage");
        cone14.setBase_hp(28);
        cone14.setBase_atk(20);
        cone14.setBase_def(22);
        cone14.setHp_growth(14);
        cone14.setAtk_growth(14);
        cone14.setDef_growth(12);
        cone14.setRarity(4);
        cone14.setImageResource(getDrawableCone(14));

    Cone cone15 = new Cone();
        cone15.setName("Odyssey of the Ancestral");
        cone15.setDescription("Wrought from the stuff of legends, this artifact carries the wisdom of forgotten lineages. It sings the unspoken histories of those who came before, invoking a sense of kinship that transcends time and space, enriching the present by honoring the past.");
        cone15.setAbility("Ancestral Wisdom: Gains 20% ATK, 20% DEF, and 20% HP when a team member is defeated");
        cone15.setBase_hp(32);
        cone15.setBase_atk(24);
        cone15.setBase_def(20);
        cone15.setHp_growth(16);
        cone15.setAtk_growth(12);
        cone15.setDef_growth(10);
        cone15.setRarity(4);
        cone15.setImageResource(getDrawableCone(15));

    Cone cone16 = new Cone();
        cone16.setName("Aetherial Quasar");
        cone16.setDescription("Imbued with the pulsating energy of a distant celestial object, it serves as a bridge between the mortal plane and the cosmic vastness. Those fortunate enough to unlock its secrets may find themselves contemplating the untapped possibilities that stretch out across the endless frontiers of the universe.");
        cone16.setAbility("Cosmic Resonance: Gains 30% ATK, 30% DEF, and 30% Crit Rate when cosmic energy aligns");
        cone16.setBase_hp(30);
        cone16.setBase_atk(28);
        cone16.setBase_def(26);
        cone16.setHp_growth(18);
        cone16.setAtk_growth(16);
        cone16.setDef_growth(16);
        cone16.setRarity(4);
        cone16.setImageResource(getDrawableCone(16));


    Cone cone1 = new Cone();
        cone1.setName("Threads of Fate");
        cone1.setDescription("The tapestry of life unfolds, woven by the delicate strings of destiny. To the untrained eye, these strings are scattered and in disarray. But those who understand see a grand design hidden within. One by one, they come together, forming the fabric of our existence, knotted and intertwined in the eternal dance of destiny.");
        cone1.setAbility("Destiny's Embrace: Increases CRIT Rate by 30% when hp is full");
        cone1.setBase_hp(19);
        cone1.setBase_atk(23);
        cone1.setBase_def(16);
        cone1.setHp_growth(12);
        cone1.setAtk_growth(12);
        cone1.setDef_growth(10);
        cone1.setRarity(3);
        cone1.setImageResource(getDrawableCone(1));*/
}
