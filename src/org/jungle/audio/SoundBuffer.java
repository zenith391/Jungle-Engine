package org.jungle.audio;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.AL11.*;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import org.jungle.util.Utils;
import org.lwjgl.openal.AL11;
import org.lwjgl.stb.STBVorbis;
import org.lwjgl.stb.STBVorbisInfo;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

public class SoundBuffer {

	private final int bufferId;
	private ShortBuffer pcm;
	private ByteBuffer vorbis;
	
	/**
	 * Only works with Vorbis .ogg
	 * @param file
	 * @throws Exception
	 */
	public SoundBuffer(File file) throws Exception {
		bufferId = alGenBuffers();
		
		try (STBVorbisInfo info = STBVorbisInfo.malloc()) {
			ShortBuffer pcm = readVorbis(file.toString(), 4096, info);
			alBufferData(bufferId, info.channels() == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16, pcm, info.sample_rate());
		}
	}
	
	public int getBufferID() {
		return bufferId;
	}
	
	public void cleanup() {
		AL11.alDeleteBuffers(bufferId);
		if (pcm != null) {
			MemoryUtil.memFree(pcm);
		}
	}
	
	private ShortBuffer readVorbis(String resource, int bufferSize, STBVorbisInfo info) throws Exception {
        try (MemoryStack stack = MemoryStack.stackPush()) {
			vorbis = Utils.ioResourceToByteBuffer(resource, bufferSize);
            IntBuffer error = stack.mallocInt(1);
            long decoder = STBVorbis.stb_vorbis_open_memory(vorbis, error, null);
            if (decoder == MemoryUtil.NULL) {
                throw new RuntimeException("Failed to open Ogg Vorbis file. Error: " + error.get(0));
            }

            STBVorbis.stb_vorbis_get_info(decoder, info);

            int channels = info.channels();

            int lengthSamples = STBVorbis.stb_vorbis_stream_length_in_samples(decoder);

            pcm = MemoryUtil.memAllocShort(lengthSamples);

            pcm.limit(STBVorbis.stb_vorbis_get_samples_short_interleaved(decoder, channels, pcm) * channels);
            STBVorbis.stb_vorbis_close(decoder);

            return pcm;
        }
	}
	
}
