# Repackage classes into the default package to reduce the size of descriptors.
-repackageclasses

-keep class org.jaudiotagger.tag.** {
    public <init>(...);
}
