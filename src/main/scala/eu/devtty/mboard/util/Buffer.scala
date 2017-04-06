package eu.devtty.mboard.util

import io.scalajs.nodejs.buffer.Buffer

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.typedarray.ArrayBuffer
import scala.scalajs.js.|

@js.native
@JSImport("buffer", "Buffer")
object Buffer extends js.Object {
  /////////////////////////////////////////////////////////////////////////////////
  //      Properties
  /////////////////////////////////////////////////////////////////////////////////

  /**
    * Returns the maximum number of bytes that will be returned when buf.inspect() is called.
    * This can be overridden by user modules. See util.inspect() for more details on buf.inspect() behavior.
    *
    * Note that this is a property on the buffer module returned by require('buffer'), not on the
    * Buffer global or a Buffer instance.
    */
  val INSPECT_MAX_BYTES: Int = js.native

  /**
    * On 32-bit architectures, this value is (2^30)-1 (~1GB). On 64-bit architectures, this value is (2^31)-1 (~2GB).F
    * Note that this is a property on the buffer module returned by require('buffer'), not on the Buffer global or a Buffer instance.
    */
  val kMaxLength: Int = js.native

  /**
    * This is the number of bytes used to determine the size of pre-allocated, internal Buffer instances used for pooling.
    * This value may be modified.
    */
  var poolSize: Int = js.native

  /////////////////////////////////////////////////////////////////////////////////
  //      Methods
  /////////////////////////////////////////////////////////////////////////////////

  /**
    * Allocates a new Buffer of size bytes. If fill is undefined, the Buffer will be zero-filled.
    * @param size     The desired length of the new Buffer
    * @param fill     A value to pre-fill the new Buffer with. Default: 0
    * @param encoding If fill is a string, this is its encoding. Default: 'utf8'
    * @return a new [[Buffer]]
    * @example {{{ Buffer.alloc(size[, fill[, encoding]]) }}}
    */
  def alloc(size: Int, fill: Buffer | Int | String = js.native, encoding: String = js.native): Buffer = js.native

  /**
    * Calling Buffer.alloc(size) can be significantly slower than the alternative Buffer.allocUnsafe(size) but ensures
    * that the newly created Buffer instance contents will never contain sensitive data.
    * @param size the allocated size.
    * @example Buffer.allocUnsafe(size)
    */
  def allocUnsafe(size: Int): Buffer = js.native

  /**
    * Allocates a new non-zero-filled and non-pooled Buffer of size bytes. The size must be less than or equal to the
    * value of require('buffer').kMaxLength (on 64-bit architectures, kMaxLength is {{{ (2^31)-1). }}} Otherwise, a RangeError
    * is thrown. A zero-length Buffer will be created if a size less than or equal to 0 is specified.
    *
    * The underlying memory for Buffer instances created in this way is not initialized. The contents of the newly
    * created Buffer are unknown and may contain sensitive data. Use buf.fill(0) to initialize such Buffer instances to zeroes.
    *
    * When using Buffer.allocUnsafe() to allocate new Buffer instances, allocations under 4KB are, by default, sliced
    * from a single pre-allocated Buffer. This allows applications to avoid the garbage collection overhead of creating
    * many individually allocated Buffers. This approach improves both performance and memory usage by eliminating the
    * need to track and cleanup as many Persistent objects.
    *
    * However, in the case where a developer may need to retain a small chunk of memory from a pool for an indeterminate
    * amount of time, it may be appropriate to create an un-pooled Buffer instance using Buffer.allocUnsafeSlow() then
    * copy out the relevant bits.
    * @param size the allocated size.
    * @example Buffer.allocUnsafeSlow(size)
    */
  def allocUnsafeSlow(size: Int): Buffer = js.native

  /**
    * Returns the actual byte length of a string. This is not the same as String.prototype.length since that returns
    * the number of characters in a string.
    * @param string   <String> | <Buffer> | <TypedArray> | <DataView> | <ArrayBuffer>
    * @param encoding the optional encoding (default "utf8")
    * @example Buffer.byteLength(string[, encoding])
    */
  def byteLength(string: js.Any, encoding: String = "utf8"): Int = js.native

  /**
    * Compares buf1 to buf2 typically for the purpose of sorting arrays of Buffers.
    * This is equivalent is calling buf1.compare(buf2).
    */
  def compare(buf1: Buffer, buf2: Buffer): Int = js.native

  /**
    * Returns a new Buffer which is the result of concatenating all the Buffers in the list together.
    * If the list has no items, or if the totalLength is 0, then a new zero-length Buffer is returned.
    * If totalLength is not provided, it is calculated from the Buffers in the list. This, however, adds an additional
    * loop to the function, so it is faster to provide the length explicitly.
    * @param list        the list of Buffer objects to concat
    * @param totalLength the optional total length
    * @example Buffer.concat(list[, totalLength])
    */
  def concat(list: js.Array[Buffer], totalLength: Int): Buffer = js.native

  /**
    * Returns a new Buffer which is the result of concatenating all the Buffers in the list together.
    * If the list has no items, or if the totalLength is 0, then a new zero-length Buffer is returned.
    * If totalLength is not provided, it is calculated from the Buffers in the list. This, however, adds an additional
    * loop to the function, so it is faster to provide the length explicitly.
    * @param list the list of Buffer objects to concat
    * @example Buffer.concat(list[, totalLength])
    */
  def concat(list: js.Array[Buffer]): Buffer = js.native

  /**
    * When passed a reference to the .buffer property of a TypedArray instance, the newly created Buffer
    * will share the same allocated memory as the TypedArray.
    * @example {{{ Buffer.from(arrayBuffer[, byteOffset[, length]]) }}}
    **/
  def from(arrayBuffer: ArrayBuffer, byteOffset: Int, length: Int): Buffer = js.native

  /**
    * When passed a reference to the .buffer property of a TypedArray instance, the newly created Buffer
    * will share the same allocated memory as the TypedArray.
    * @example {{{ Buffer.from(arrayBuffer[, byteOffset[, length]]) }}}
    **/
  def from(arrayBuffer: ArrayBuffer, byteOffset: Int): Buffer = js.native

  /**
    * When passed a reference to the .buffer property of a TypedArray instance, the newly created Buffer
    * will share the same allocated memory as the TypedArray.
    * @example {{{ Buffer.from(arrayBuffer[, byteOffset[, length]]) }}}
    **/
  def from(arrayBuffer: ArrayBuffer): Buffer = js.native

  /**
    * Allocates a new Buffer using an array of octets.
    * @example Buffer.from(array)
    */
  def from(array: js.Array[Int]): Buffer = js.native

  /**
    * Creates a new Buffer containing the given JavaScript string str. If provided, the encoding parameter identifies
    * the strings character encoding.
    * @param str      the source string
    * @param encoding the given encoding
    * @return a new Buffer
    */
  def from(str: String, encoding: String = js.native): Buffer = js.native

  /**
    * Returns true if obj is a Buffer, false otherwise.
    * @param obj the given object
    * @return true if obj is a Buffer, false otherwise.
    */
  def isBuffer(obj: js.Any): Boolean = js.native

  /**
    * Returns true if encoding contains a supported character encoding, or false otherwise.
    * @param encoding A character encoding name to check
    * @return true if encoding contains a supported character encoding, or false otherwise.
    */
  def isEncoding(encoding: String): Boolean = js.native

  /**
    * Re-encodes the given Buffer instance from one character encoding to another. Returns a new Buffer instance.
    *
    * Throws if the fromEnc or toEnc specify invalid character encodings or if conversion from fromEnc to toEnc
    * is not permitted.
    *
    * The transcoding process will use substitution characters if a given byte sequence cannot be adequately
    * represented in the target encoding.
    * @param source  A Buffer instance
    * @param fromEnc The current encoding
    * @param toEnc   To target encoding
    * @return a new Buffer instance.
    */
  def transcode(source: Buffer, fromEnc: String, toEnc: String): Buffer = js.native
}
