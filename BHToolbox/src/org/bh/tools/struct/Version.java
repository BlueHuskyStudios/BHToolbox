package org.bh.tools.struct;

import bht.tools.util.Comparable64;
import org.bh.tools.util.ArrayPP;
import java.util.Arrays;
import java.util.regex.Pattern;
import static org.bh.tools.math.NumberConverter.to32Bit;
import static org.bh.tools.struct.Version.Channel.STABLE;

/**
 * Version, made for BHToolbox, is copyright Blue Husky Programming ©2014 BH-1-PS<hr>
 *
 * @author Kyli of Blue Husky Programming
 * @version 2.0.0  <pre>
 *     - 2.0.0 (2016-04-24) - Kyli Rouge updated to modern BH standards
 *     - 1.1.1 (2014-11-29) - Kyli Rouge Changed version pattern from \d(\.\d)* to \d+(\.\d+)*
 *     - 1.1.0 (2014-11-29) - Kyli Rouge added support for channels
 * </pre>
 *
 * @since 2014-09-22
 */
public class Version implements Comparable<Version>, Comparable64<Version> { // MUST BE COMPILED IN UNICODE

    /**
     * Defines what a version looks like: {@code \d+(\.\d+)*}
     */
    public static final Pattern VERSION_PATTERN = Pattern.compile("\\d+(\\.\\d+)*");

    protected Long[] stages;
    private String cache;
    protected Channel channel = STABLE;

    /**
     * Creates a version with the given stages. For instance, if it's version 1.2.3, you would call
     * {@code new Version(1,2,3)}
     *
     * @param initStages The stages (number) of the version
     */
    public Version(Long... initStages) {
        this(STABLE, initStages);
    }

    /**
     * Creates a version with the given stages. For instance, if it's version 1.2.3, you would call
     * {@code new Version(1,2,3)}
     *
     * @param initStages The stages (number) of the version
     */
    public Version(long... initStages) {
        this(STABLE, initStages);
    }

    /**
     * Creates a version with the given channel and stages. For instance, if it's version 1.2.3 β, you would call
     * {@code new Version(β, 1,2,3)}
     *
     * @param initChannel The channel of the version
     * @param initStages  The stages (number) of the version
     */
    public Version(Channel initChannel, Long... initStages) {
        stages = initStages;
        channel = initChannel;
    }

    /**
     * Creates a version with the given channel and stages. For instance, if it's version 1.2.3 β, you would call
     * {@code new Version(β, 1,2,3)}
     *
     * @param initChannel The channel of the version
     * @param initStages  The stages (number) of the version
     */
    public Version(Channel initChannel, long... initStages) {
        stages = new Long[initStages.length];
        for (int stage = 0; stage < stages.length; stage++) {
            stages[stage] = initStages[stage];
        }
        channel = initChannel;
    }

    /**
     * Returns the channel that this version is on
     *
     * @return the channel that this version is on
     */
    public Channel getChannel() {
        return channel;
    }

    /**
     * Returns a string representation of the version, like: {@code "1.2.3β"}
     *
     * @return a string representation of the version
     */
    @Override
    public String toString() {
        if (cache != null) {
            return cache;
        }
        return cache = new ArrayPP<>(stages).toString("", ".", "").concat(Character.toString(channel.UNICODE));
    }


    /**
     * Converts the given string into a Version. The string must be formatted to this regex: {@code /\d(\.\d)*}{@code /}
     * <!-- Real talk? I don't like how Java requires this in two separate {@code}s. -->
     * <br/><br/>
     * Currently, this does not support channels. This means that this is not symmetric with {@link #toString}
     *
     * @param versionString A valid version string (e.g. {@code 12}, {@code 1.2}, {@code 1.23.4567}, etc.)
     * @return A {@link Version} made from the given string
     * @throws IllegalArgumentException if the given string does not match the regex {@code /\d(\.\d)*}{@code /}
     * @throws NumberFormatException    If an error occurs when parsing the numbers of the version string. Try to keep
     *                                  them 32-bit ;3
     */
    public static Version fromString(String versionString) throws IllegalArgumentException {
        if (!VERSION_PATTERN.matcher(versionString).matches()) {
            // TODO: Find a more friendly way to handle this. Perhaps remove/ignore unvalid characters?
            throw new IllegalArgumentException("Given string is not a valid version number: " + versionString);
        }

        String[] numStrings = Pattern.compile("\\.").split(versionString);
        Long[] nums = new Long[numStrings.length];

        for (int i = 0; i < numStrings.length; i++) {
            nums[i] = Long.valueOf(numStrings[i]);
        }
        return new Version(nums);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Arrays.deepHashCode(this.stages);
        return hash;
    }

    /**
     * Evaluates whether these are equal. Evaluation happens in this order:
     * <OL>
     * <LI>check with == (yes? true)</LI>
     * <LI>whether obj is null (yes? false)</LI>
     * <LI>whether they're the same class (no? false)</LI>
     * <LI>whether their channels are equal (no? false)</LI>
     * <LI>whether their stages are equal (no? false)</LI>
     * <LI>if all of the above are not tripped, return true</LI>
     * </OL>
     *
     * @param obj the other object to test
     * @return {@code true} iff both this and the other object are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Version other = (Version) obj;
        if (!this.channel.equals(other.channel)) {
            return false;
        }
        if (!Arrays.deepEquals(this.stages, other.stages)) {
            return false;
        }
        return true;
    }


    /**
     * <ol>
     * <li>If the given object is this object, {@code 0} is returned</li>
     * <li>Else, if the given object's channel is different from the other Version's, the difference in ordinals is
     * returned</li>
     * <li>Else, if any one of this Version's stages is different from the other Version's corresponding stage, the
     * difference is returned</li>
     * <li>Else, the difference between the number of stages is returned.</li>
     * </ol>
     *
     * @param otherVersion the other object to compare this one against
     * @return an integer, centered around 0, telling how much more or less this object is than the other.
     */
    @Override
    public int compareTo(Version otherVersion) {
        return to32Bit(compareTo64(otherVersion));
    }

    @Override
    public long compareTo64(Version otherVersion) {
        if (equals(otherVersion)) {
            return 0;
        }
        if (channel != otherVersion.channel) {
            return channel.ordinal() - otherVersion.channel.ordinal();
        }
        for (int i = 0, l = Math.min(stages.length, otherVersion.stages.length); i < l; i++) {
            if (!stages[i].equals(otherVersion.stages[i])) {
                return stages[i] - otherVersion.stages[i];
            }
        }
        return stages.length - otherVersion.stages.length;
    }

    /**
     * Represents a channel (stable, beta, alpha, lambda)
     */
    public static enum Channel {
        /**
         * Signifies that this software is in very unstable or incomplete testing, and the next major release should be
         * an unstable alpha test.
         *
         * <DL>
         * <DT>ASCII</DT>
         * <DD>{@code l} (108)</DD>
         * <DT>Unicode</DT>
         * <DD>U+03BB Greek Small Letter Lambda</DD>
         * <DT>HTML</DT>
         * <DD>{@code "&lambda;"}</DD>
         * </DL>
         */
        λ('l', 'λ', "&lambda;"),
        /**
         * Signifies that this software is in unstable alpha testing, and the next major release should be a little more
         * stable (beta).
         *
         * <DL>
         * <DT>ASCII</DT>
         * <DD>{@code a} (97)</DD>
         * <DT>Unicode</DT>
         * <DD>U+03B1 Greek Small Letter Alpha</DD>
         * <DT>HTML</DT>
         * <DD>{@code "&alpha;"}</DD>
         * </DL>
         */
        α('a', 'α', "&alpha;"),
        /**
         * Signifies that this software is in beta testing, and the next major release should be stable.
         *
         * <DL>
         * <DT>ASCII</DT>
         * <DD>{@code b} (98)</DD>
         * <DT>Unicode</DT>
         * <DD>U+03B2 Greek Small Letter Beta</DD>
         * <DT>HTML</DT>
         * <DD>{@code "&beta;"}</DD>
         * </DL>
         */
        β('b', 'β', "&beta;"),
        /**
         * Signifies that this software is stable. This is the only channel without a symbol.
         *
         * <DL>
         * <DT>ASCII</DT>
         * <DD>space (20)</DD>
         * <DT>Unicode</DT>
         * <DD>U+200C Zero-Width Non-Joiner</DD>
         * <DT>HTML</DT>
         * <DD>the empty string ({@code ""})</DD>
         * </DL>
         */
        STABLE(' ', (char) 0x200C, "");

        /**
         * The ASCII symbol of the channel.
         */
        public final char ASCII;

        /**
         * The Unicode symbol of the channel.
         */
        public final char UNICODE;

        /**
         * The HTML escape of the Unicode symbol of the channel.
         */
        public final String HTML;

        /**
         * Creates a new Channel with the given translations.
         *
         * @param ascii   The ASCII symbol of the channel.
         * @param unicode The Unicode symbol of the channel. This is preferred.
         * @param html    The HTML escape of the Unicode symbol of the channel.
         */
        private Channel(char ascii, char unicode, String html) {
            ASCII = ascii;
            UNICODE = unicode;
            HTML = html;
        }
    }

    public class MutableVersion extends Version {

        /**
         * Creates a mutable version with the given stages. For instance, if it's version 1.2.3, you would call
         * {@code new MutableVersion(1,2,3)}
         *
         * @param initStages The stages (number) of the version
         */
        public MutableVersion(Long... initStages) {
            super(STABLE, initStages);
        }

        /**
         * Creates a mutable version with the given stages. For instance, if it's version 1.2.3, you would call
         * {@code new MutableVersion(1,2,3)}
         *
         * @param initStages The stages (number) of the version
         */
        public MutableVersion(long... initStages) {
            super(STABLE, initStages);
        }

        /**
         * Creates a mutable version with the given channel and stages. For instance, if it's version 1.2.3 β, you would
         * call {@code new MutableVersion(β, 1,2,3)}
         *
         * @param initChannel The channel of the version
         * @param initStages  The stages (number) of the version
         */
        public MutableVersion(Channel initChannel, Long... initStages) {
            super(initChannel, initStages);
        }

        /**
         * Creates a mutable version with the given channel and stages. For instance, if it's version 1.2.3 β, you would
         * call {@code new MutableVersion(β, 1,2,3)}
         *
         * @param initChannel The channel of the version
         * @param initStages  The stages (number) of the version
         */
        public MutableVersion(Channel initChannel, long... initStages) {
            super(initChannel, initStages);
        }


        /**
         * Changes the channel that this version is on
         *
         * @param newChannel the new channel for this version to be on
         * @return {@code this}
         */
        public Version setChannel(Channel newChannel) {
            channel = newChannel;
            cache = null;
            return this;
        }
    }
}
