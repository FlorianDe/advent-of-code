from typing import Optional

import time


def timed(task: Optional[str] = None):

    def decorator(method):
        def timed_method(*args, **kw):
            method_name = task if method is not None else f"{method.__name__}:"
            ts = time.time()
            result = method(*args, **kw)
            te = time.time()
            if 'log_time' in kw:
                name = kw.get('log_name', method_name)
                kw['log_time'][name] = int((te - ts) * 1000)
            else:
                print(f"{method_name} {result} took {(te - ts) * 1000:.3f} ms")
            return result

        return timed_method
    return decorator
